package com.ap.mobile.stocks.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.FragmentMarketViewBinding
import com.ap.mobile.stocks.databinding.RecyclerviewMarketViewBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStockNewsListBinding
import com.ap.mobile.stocks.ui.base.BaseFragment
import com.ap.mobile.stocks.ui.detail.StockDetailsActivity
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable

/**
 * A simple [Fragment] subclass.
 *
 */
class MarketViewFragment : BaseFragment<NetworkViewModel, FragmentMarketViewBinding>() {
    override fun getViewModel(): Class<NetworkViewModel> = NetworkViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.fragment_market_view

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.refresh.setOnRefreshListener {
            if(dataBinding.refresh.isRefreshing) {
                dataBinding.refresh.isRefreshing = false
            }
        }
        setupMostActive()
        setGainers()
        setLosers()
        setNews()
    }

    private fun setupMostActive() {
        viewModel.getMostActiveStocks().observe(this, Observer {
            if(it == null) {
                dataBinding.popularCardView.visibility = View.GONE
                return@Observer
            }
            val data = it
            dataBinding.popular?.stockDetailRv?.apply {
                adapter = RxSimpleAdapter(
                        Observable.fromArray(data),
                        data,
                        RecyclerviewMarketViewBinding::inflate,
                        RecyclerviewMarketViewBinding::setList
                ).apply {
                    clicks.bindToLifecycle(rootView).subscribe {
                        startActivity(StockDetailsActivity.newIntent(context, it.symbol!!, it.companyName!!))
                    }
                }
            }

        })
    }

    private fun setGainers() {
        viewModel.getGainersStocks().observe(this, Observer {
            if(it == null) {
                dataBinding.gainersCardView.visibility = View.GONE
                return@Observer
            }
            val data = it
            dataBinding.gainers?.stockDetailRv?.apply {
                adapter = RxSimpleAdapter(
                        Observable.fromArray(data),
                        data,
                        RecyclerviewMarketViewBinding::inflate,
                        RecyclerviewMarketViewBinding::setList
                ).apply {
                    clicks.bindToLifecycle(rootView).subscribe {
                        startActivity(StockDetailsActivity.newIntent(context, it.symbol!!, it.companyName!!))
                    }
                }
            }

        })
    }

    private fun setLosers() {
        viewModel.getLosersStocks().observe(this, Observer {
                if(it == null) {
                dataBinding.losersCardView.visibility = View.GONE
                return@Observer
            }
            val data = it
            dataBinding.losers?.stockDetailRv?.apply {
                adapter = RxSimpleAdapter(
                        Observable.fromArray(data),
                        data,
                        RecyclerviewMarketViewBinding::inflate,
                        RecyclerviewMarketViewBinding::setList
                ).apply {
                    clicks.bindToLifecycle(rootView).subscribe {
                        startActivity(StockDetailsActivity.newIntent(context, it.symbol!!, it.companyName!!))
                    }
                }
            }

        })
    }

    private fun setNews() {
        viewModel.getLatestNews().observe(this, Observer {
            if(it == null) {
                dataBinding.newsCardView.visibility = View.GONE
                return@Observer
            }
            val data = it
            dataBinding.news?.stockDetailRv?.apply {
                adapter = RxSimpleAdapter(
                        Observable.fromArray(data),
                        data,
                        RecyclerviewStockNewsListBinding::inflate,
                        RecyclerviewStockNewsListBinding::setNews
                ).apply {
                    clicks.bindToLifecycle(rootView).subscribe {
                        dataBinding.news?.webview?.visibility = View.VISIBLE
                        dataBinding.news?.webview?.loadUrl(it.url)
                    }
                }
            }
        })
    }
}
