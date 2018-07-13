package com.ap.mobile.stocks.ui.main


import android.app.AlertDialog
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

class MainActivity : BaseActivityWithVM<NetworkViewModel, ActivityMainBinding>() {

    override fun getViewModel(): Class<NetworkViewModel> = NetworkViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dataBinding.toolbar)
    }

    override fun onStart() {
        super.onStart()
        // check if there is internet access
        if(NetworkUtil.getConnectionStatus(this) == 0) {
            showErrorDialog("You are not connected to the Internet. None of the functionality will work.")
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
                        shouldClearOnClose = false
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

    /**
     * show error
     */
    private fun showErrorDialog(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
                "ok",
                { dialog, _ -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()
    }
}
