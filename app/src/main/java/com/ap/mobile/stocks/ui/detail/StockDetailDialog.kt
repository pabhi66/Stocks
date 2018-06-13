package com.ap.mobile.stocks.ui.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.DialogStockDetailBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStockNewsListBinding
import com.ap.mobile.stocks.ui.base.BaseBottomSheetDialogFragment
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import android.support.v7.app.AppCompatActivity
import com.ap.mobile.stocks.data.local.entity.NewsItem
import com.ap.mobile.stocks.data.local.entity.Stock


/**
 *
 * class information...
 */
class StockDetailDialog: BaseBottomSheetDialogFragment<StockDetailViewModel, DialogStockDetailBinding>() {

    private lateinit var symbol: String

    override fun getViewModel() = StockDetailViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.dialog_stock_detail

    companion object {
        private const val SYMBOL = "symbol"
        fun newInstance(symbol: String): StockDetailDialog {
            val fragment = StockDetailDialog()
            val args = Bundle()
            args.putString(SYMBOL, symbol)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        symbol = arguments?.get(SYMBOL) as String
        viewModel.getStockData(symbol).observe(this,
            Observer {
                if(it != null) {
                    setup(it)
                    setupNewsRecyclerView(it)
                }
            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val ab = (activity as AppCompatActivity).supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
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
        dataBinding.stockDetailPercentage.text = "${stock.quote.changePercent}%"
    }

    private fun setupNewsRecyclerView(stock: Stock) {
        var news: List<NewsItem>? = null
        if(stock.news != null) {
            if(stock.news.isNotEmpty())
                news = stock.news as List<NewsItem>?
        }
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