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
        if(quote?.week52High != null) {
            dataBinding.stockDetail52highValue.text = df.format(quote.week52High)
        } else {
            dataBinding.stockDetail52highValue.text = "-"
        }
        if(quote?.week52Low != null) {
            dataBinding.stockDetail52lowValue.text = df.format(quote.week52Low)
        } else {
            dataBinding.stockDetail52lowValue.text = "-"
        }
        if(quote?.latestVolume != null) {
            val volume = quote.latestVolume.toDouble().div(1000000)
            dataBinding.stockDetailVolumeValue.text = df.format(volume) + "M"
        } else {
            dataBinding.stockDetailVolumeValue.text = "-"
        }
        if(quote?.avgTotalVolume != null) {
            val avgVolume = quote.avgTotalVolume.toDouble().div(1000000)
            dataBinding.stockDetailAvgVolumeValue.text = df.format(avgVolume) + "M"
        } else {
            dataBinding.stockDetailAvgVolumeValue.text = "-"
        }
        if(quote?.marketCap != null) {
            val cap = quote.marketCap.toDouble().div(1000000000)
            dataBinding.stockDetailMktcapValue.text = df.format(cap) + "B"
        } else {
            dataBinding.stockDetailMktcapValue.text = "-"
        }
        dataBinding.stockDetailPeratioValue.text = quote?.peRatio.toString()
        if(quote?.ytdChange != null) {
            dataBinding.stockDetailYtdValue.text = df.format(quote.ytdChange).toString()
        } else {
            dataBinding.stockDetailYtdValue.text = "-"
        }
        dataBinding.stockDetailDivRateValue.text = stock.stats?.dividendRate.toString()
        if(stock.stats?.dividendYield != null) {
            dataBinding.stockDetailDivYieldValue.text = df.format(stock.stats.dividendYield)
        } else {
            dataBinding.stockDetailDivYieldValue.text = "-"
        }

    }

    private fun setupFinancial(stock: Stock) {
        if(stock.financials?.financials?.isNotEmpty()!!) {
            val stat = stock.financials.financials[0]
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            dataBinding.stockDetailReportDateValue.text = stat?.reportDate
            if(stat?.grossProfit != null) {
                dataBinding.stockDetailGrossProfitValue.text = "${df.format(stat.grossProfit.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailGrossProfitValue.text = "-"
            }
            if(stat?.costOfRevenue != null) {
                dataBinding.stockDetailCostOfRevenueValue.text = "${df.format(stat.costOfRevenue.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailCostOfRevenueValue.text = "-"
            }
            if(stat?.operatingRevenue != null) {
                dataBinding.stockDetailOperatingRevenueValue.text = "${df.format(stat.operatingRevenue.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailOperatingRevenueValue.text = "-"
            }
            if(stat?.totalRevenue != null) {
                dataBinding.stockDetailTotalRevenueValue.text = "${df.format(stat.totalRevenue.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailTotalRevenueValue.text = "-"
            }
            if(stat?.operatingIncome != null) {
                dataBinding.stockDetailOperatingIncomeValue.text = "${df.format(stat.operatingIncome.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailOperatingIncomeValue.text = "-"
            }
            if(stat?.netIncome != null) {
                dataBinding.stockDetailNetIncomeValue.text = "${df.format(stat.netIncome.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailNetIncomeValue.text = "-"
            }
            if(stat?.researchAndDevelopment != null) {
                dataBinding.stockDetailRdValue.text = "${df.format(stat.researchAndDevelopment.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailRdValue.text = "-"
            }
            if(stat?.operatingExpense != null) {
                dataBinding.stockDetailOperatingExpenseValue.text = "${df.format(stat.operatingExpense.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailOperatingExpenseValue.text = "-"
            }
            if(stat?.currentAssets != null) {
                dataBinding.stockDetailCurrentAssetsValue.text = "${df.format(stat.currentAssets.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailCurrentAssetsValue.text = "-"
            }
            if(stat?.totalAssets != null) {
                dataBinding.stockDetailTotaltAssetsValue.text = "${df.format(stat.totalAssets.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailTotaltAssetsValue.text = "-"
            }
            if(stat?.currentCash != null) {
                dataBinding.stockDetailCurrentCashValue.text = "${df.format(stat.currentCash.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailCurrentCashValue.text = "-"
            }
            if(stat?.totalCash != null) {
                dataBinding.stockDetailTotalCashValue.text = "${df.format(stat.totalCash.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailTotalCashValue.text = "-"
            }
            if(stat?.cashFlow != null) {
                dataBinding.stockDetailCashFlowValue.text = "${df.format(stat.cashFlow.toDouble().div(1000000000))}B"
            } else {
                dataBinding.stockDetailCashFlowValue.text = "-"
            }
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
        viewModel.getTodayChart(symbol).observe(this,
            Observer {
                println(it)
            })

//        viewModel.getChart(symbol, "1m").observe(this,
//            Observer {
//                println(it)
//            })
    }
}
