package com.ap.mobile.stocks.ui.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.data.local.entity.NewsItem
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.databinding.FragmentStockDetailBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStockNewsListBinding
import com.ap.mobile.stocks.ui.base.BaseFragment
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import java.math.RoundingMode
import java.text.DecimalFormat

@SuppressLint("SetTextI18n")
class StockDetailFragment : BaseFragment<StockDetailViewModel, FragmentStockDetailBinding>() {
    private lateinit var symbol: String

    companion object {
        private const val SYMBOL = "symbol"
        fun newInstance(symbol: String): StockDetailFragment {
            val fragment = StockDetailFragment()
            val args = Bundle()
            args.putString(SYMBOL, symbol)
            fragment.arguments = args
            return fragment
        }
     }

    override fun getViewModel(): Class<StockDetailViewModel> = StockDetailViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.fragment_stock_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        symbol = arguments!!.getString(SYMBOL)
        viewModel.getStockData(symbol).observe(this,
            Observer {
                if(it != null) {
                    setup(it)
                    setupNewsRecyclerView(it)
                    setupFinancial(it)
                    setupAbout(it)
                    setupGraph()
                }
            })
    }

    private fun setup(stock: Stock){
        dataBinding.stockDetailSymbol.text = stock.company?.symbol
        dataBinding.stockDetailCompany.text = stock.company?.companyName
        val latest = stock.quote?.latestPrice!!
        val open = stock.quote.open!!
        if(latest < open) {
            dataBinding.stockDetailPrice.setTextColor(ContextCompat.getColor(context!!, R.color.red))
            dataBinding.stockDetailPercentage.setTextColor(ContextCompat.getColor(context!!, R.color.red))
        } else {
            dataBinding.stockDetailPrice.setTextColor(ContextCompat.getColor(context!!, R.color.green))
            dataBinding.stockDetailPercentage.setTextColor(ContextCompat.getColor(context!!, R.color.green))
        }
        dataBinding.stockDetailPrice.text = "$" + "${stock.quote.latestPrice}"
        val percentage = stock.quote.changePercent?.times(100)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        dataBinding.stockDetailPercentage.text = "${df.format(percentage)}%"

        setupStats(stock)
    }

    private fun setupNewsRecyclerView(stock: Stock) {
        val news: List<NewsItem>?
        if(stock.news != null) {
            if(stock.news.isNotEmpty()) {
                news = stock.news as List<NewsItem>?
                dataBinding.stockDetailRv.apply {
                    adapter = RxSimpleAdapter<NewsItem, RecyclerviewStockNewsListBinding>(
                        Observable.fromArray(news),
                        news!!,
                        RecyclerviewStockNewsListBinding::inflate,
                        RecyclerviewStockNewsListBinding::setNews
                    ).apply {
                        clicks.bindToLifecycle(view!!).subscribe {
                            Toast.makeText(context, "${it.url}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupStats(stock: Stock) {
        val quote = stock.quote
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        dataBinding.stockDetailOpenValue.text = quote?.open.toString()
        dataBinding.stockDetailHighValue.text = quote?.high.toString()
        dataBinding.stockDetailLowValue.text = quote?.low.toString()
        dataBinding.stockDetail52highValue.text = df.format(quote?.week52High)
        dataBinding.stockDetail52lowValue.text = df.format(quote?.week52Low)
        val volume = quote?.latestVolume?.toDouble()?.div(1000000)
        dataBinding.stockDetailVolumeValue.text = df.format(volume) + "M"
        val avgVolume = quote?.avgTotalVolume?.toDouble()?.div(1000000)
        dataBinding.stockDetailAvgVolumeValue.text = df.format(avgVolume) + "M"
        val cap = quote?.marketCap?.toDouble()?.div(1000000000)
        dataBinding.stockDetailMktcapValue.text = df.format(cap) + "B"
        dataBinding.stockDetailPeratioValue.text = quote?.peRatio.toString()
        dataBinding.stockDetailYtdValue.text = df.format(quote?.ytdChange).toString()
        dataBinding.stockDetailDivRateValue.text = stock.stats?.dividendRate.toString()
        dataBinding.stockDetailDivYieldValue.text = df.format(stock.stats?.dividendYield)
    }

    private fun setupFinancial(stock: Stock) {
        if(stock.financials?.financials?.isNotEmpty()!!) {
            val stat = stock.financials.financials[0]
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            dataBinding.stockDetailReportDateValue.text = stat?.reportDate
            dataBinding.stockDetailGrossProfitValue.text = "${df.format(stat?.grossProfit?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailCostOfRevenueValue.text = "${df.format(stat?.costOfRevenue?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailOperatingRevenueValue.text = "${df.format(stat?.operatingRevenue?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailTotalRevenueValue.text = "${df.format(stat?.totalRevenue?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailOperatingIncomeValue.text = "${df.format(stat?.operatingIncome?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailNetIncomeValue.text = "${df.format(stat?.netIncome?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailRdValue.text = "${df.format(stat?.researchAndDevelopment?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailOperatingExpenseValue.text = "${df.format(stat?.operatingExpense?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailCurrentAssetsValue.text = "${df.format(stat?.currentAssets?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailTotaltAssetsValue.text = "${df.format(stat?.totalAssets?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailCurrentCashValue.text = "${df.format(stat?.currentCash?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailTotalCashValue.text = "${df.format(stat?.totalCash?.toDouble()?.div(1000000000))}B"
            dataBinding.stockDetailCashFlowValue.text = "${df.format(stat?.cashFlow?.toDouble()?.div(1000000000))}B"
        } else {
            dataBinding.financialCardView.visibility = View.GONE
            dataBinding.imageLabel5.visibility = View.GONE
        }
    }

    private fun setupAbout(stock: Stock) {
        dataBinding.companyDescription.text = stock.company?.description
        dataBinding.companyCeo.text = "CEO: " + stock.company?.cEO
        dataBinding.companySector.text = "SECTOR: " + stock.company?.sector
        dataBinding.companyIndustry.text = "INDUSTRY: " + stock.company?.industry
        dataBinding.companyExchange.text = "EXCHANGE: " + stock.company?.exchange
    }

    private fun setupGraph() {
//        viewModel.getTodayChart(symbol).observe(this,
//            Observer {
//                println(it)
//            })

        viewModel.getChart(symbol, "1m").observe(this,
            Observer {
                println(it)
            })
    }
}
