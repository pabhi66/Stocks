package com.ap.mobile.stocks.ui.main


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.ActivityMainBinding
import com.ap.mobile.stocks.ui.base.BaseActivityWithVM
import com.ap.mobile.stocks.ui.detail.StockDetailsActivity
import com.ap.mobile.stocks.ui.views.searchview.SearchItem
import com.ap.mobile.stocks.ui.views.searchview.bindSearchView
import com.ap.mobile.stocks.util.NetworkUtil
import com.ap.mobile.stocks.util.alert

class MainActivity : BaseActivityWithVM<NetworkViewModel, ActivityMainBinding>() {

    override fun getViewModel(): Class<NetworkViewModel> = NetworkViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dataBinding.toolbar)
        val adapter = MyFragmentAdapter(supportFragmentManager)
        val pager = dataBinding.viewPager
        pager.adapter = adapter
        dataBinding.indicator.setViewPager(pager)

    }

    override fun onResume() {
        super.onResume()
        // check if there is internet access
        if (!NetworkUtil.isInternetConnected(this)) {
            this.alert("You are not connected to the Internet. Please check your connection and try again.")
        }
    }

    override fun getMenuRes(): Int = R.menu.menu_main

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        setupSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchView(menu: Menu) {
        // FIRST GET SYMBOLS FROM THE DATABASE THEN SETUP SEARCH VIEW
        // NEEDS INTERNET CONNECTION
        // OTHERWISE SEARCH WILL NOT WORK
        viewModel.getStockSymbols().observe(this,
                Observer {
                    bindSearchView(menu, R.id.action_search){
                        textCallback = { query, searchView ->
                            val items = it?.filter {
                                it.symbol!!.startsWith(query, ignoreCase = true) || it.name!!.contains(query, ignoreCase = true)
                            }?.map {
                                SearchItem(it.symbol!!, description = it.name)
                            }
                            searchView.results = items!!
                        }
                        textDebounceInterval = 0
                        noResultsFound = R.string.no_match
                        shouldClearOnClose = true
                        foregroundColor = R.color.black1
                        onItemClick = { position, _ , content, searchView ->
                            searchView.revealClose()
                            val item = searchView.adapter.getAdapterItem(position)
                            startActivity(StockDetailsActivity.newIntent(this@MainActivity, content, item.description!!))
                        }
                    }
                })
    }

    internal inner class MyFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = arrayOfNulls<Fragment>(2)
        private val titles = arrayOf("Market View", "Watchlist")

        init {
            fragments[0] = MarketViewFragment()
            fragments[1] = WatchlistFragment()
        }

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = titles[position]

        override fun getItem(position: Int): Fragment? = fragments[position]
    }
}
