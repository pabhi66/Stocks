package com.ap.mobile.stocks.ui.main

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.FragmentMainBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStocksListBinding
import com.ap.mobile.stocks.ui.base.BaseFragment
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.ap.mobile.stocks.data.local.entity.UserStockList
import com.ap.mobile.stocks.ui.views.rxrecyclerview.ItemTouchHelperAdapter
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RecyclerViewItemTouchHelper
import com.ap.mobile.stocks.ui.views.searchview.SearchItem
import com.ap.mobile.stocks.ui.views.searchview.bindSearchView
import java.util.*
import com.ap.mobile.stocks.util.ActivityUtil


/**
 * A placeholder fragment containing a simple view.
 */
@Suppress("unused")
class MainActivityFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    companion object {
        private val TAG = MainActivityFragment::class.java.simpleName
    }

    // GET VIEW MODEL
    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    // GET LAYOUT RES
    override fun getLayoutRes(): Int = R.layout.fragment_main

    // ON CREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // set up menu
    }

    // SET UP MENU
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        setupSearchView(menu)
    }

    // ON ACTIVITY CREATED
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        setupSwipeToRefresh()
    }

    private fun setupSwipeToRefresh() {
        dataBinding.swiperefresh.setOnRefreshListener {
            setupRecyclerView()
        }
    }

    /**
     * set up recycler view with adapter and on click listener
     * also set up swipe and drag listeners
     */
    private fun setupRecyclerView() {
        // get user's stock list
        viewModel.getUserStockList().observe(
            this , Observer {
                    // if list is not null set up recycler view
                    if(it != null) {
                        dataBinding.stocksFavRecyclerView.apply {
                            adapter = RxSimpleAdapter(
                                Observable.fromArray(viewModel.userStocksList.value!!),
                                viewModel.userStocksList.value!!,
                                RecyclerviewStocksListBinding::inflate,
                                RecyclerviewStocksListBinding::setViewModel,
                                isUserStockList = true,
                                stocksRepository = viewModel.stocksRepository
                            ).apply {
                                clicks.bindToLifecycle(view!!).subscribe {
                                    // ActivityUtil.addFragmentToActivity(fragmentManager!!, StockDetailFragment.newInstance(it.symbol), R.id.fragment)
                                }
                            }
                        }
                    }

            // set fixed size to false
            dataBinding.stocksFavRecyclerView.setHasFixedSize(false)

            // set up drag and swipe listener
            val callback = RecyclerViewItemTouchHelper(object: ItemTouchHelperAdapter {
                /**
                 * recycler view on item dragged
                 */
                override fun onItemMove(fromPosition: Int, toPosition: Int) {
                    // must call
                    if(fromPosition < toPosition) {
                        for (i in fromPosition until toPosition) {
                            Collections.swap(viewModel.userStocksList.value, i, i + 1)
                        }
                    } else {
                        for (i in fromPosition downTo toPosition + 1) {
                            Collections.swap(viewModel.userStocksList.value, i, i - 1)
                        }
                    }
                    dataBinding.stocksFavRecyclerView.adapter.notifyItemMoved(fromPosition, toPosition)
                }

                /**
                 * recycler view item swiped
                 */
                override fun onItemDismiss(position: Int) {
                    // must call
                    viewModel.deleteStock(viewModel.getUserStockList().value!![position].symbol)
                    dataBinding.stocksFavRecyclerView.adapter.notifyItemRemoved(position)
                }

            })
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(dataBinding.stocksFavRecyclerView)
        })
        if(dataBinding.swiperefresh.isRefreshing) {
            dataBinding.swiperefresh.isRefreshing = false
        }
    }

    // SWT UP SEARCH VIEW
    private fun setupSearchView(menu: Menu) {
        // FIRST GET SYMBOLS FROM THE DATABASE THEN SETUP SEARCH VIEW
        // NEEDS INTERNET CONNECTION
        // OTHERWISE SEARCH WILL NOT WORK
        viewModel.getStockSymbols().observe(this,
            Observer {
                activity?.bindSearchView(menu, R.id.action_search){
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
                    onItemClick = { _, _, content, searchView ->
                        searchView.revealClose()
                        // ActivityUtil.addFragmentToActivity(fragmentManager!!, StockDetailFragment.newInstance(content), R.id.fragment)
                        viewModel.insertStockToUserList(content, this@MainActivityFragment)
                    }
                }
            })
    }
}
