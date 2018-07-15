package com.ap.mobile.stocks.ui.main


import android.arch.lifecycle.Observer
import android.os.Bundle
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
                                it.symbol!!.startsWith(query, ignoreCase = true) || it.name!!.startsWith(query, ignoreCase = true)
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
                            // ActivityUtil.addFragmentToActivity(fragmentManager!!, StockDetailFragment.newInstance(content), R.id.fragment)
                            // viewModel.insertStockToUserList(content, this@MainActivity)
                            val item = searchView.adapter.getAdapterItem(position)
                            startActivity(StockDetailsActivity.newIntent(this@MainActivity, content, item.description!!))
                        }
                    }
                })
    }
}
