package com.ap.mobile.stocks.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.FragmentMainBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStocksListBinding
import com.ap.mobile.stocks.ui.base.BaseFragment
import com.ap.mobile.stocks.ui.detail.StockDetailsActivity
import com.ap.mobile.stocks.ui.views.rxrecyclerview.ItemTouchHelperAdapter
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RecyclerViewItemTouchHelper
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.ap.mobile.stocks.util.toast
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable

class MainFragment: BaseFragment<NetworkViewModel, FragmentMainBinding>() {
    override fun getViewModel(): Class<NetworkViewModel> = NetworkViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.fragment_main

    private var shouldExecuteOnResume = false

    // ON ACTIVITY CREATED
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        setupSwipeToRefresh()
    }

    private fun setupSwipeToRefresh() {
        dataBinding.swiperefresh.setOnRefreshListener {
            setupRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        // setupRecyclerView()
        if(shouldExecuteOnResume){
            // Your onResume Code Here
            setupRecyclerView()
        } else{
            shouldExecuteOnResume = true
        }
    }

    /**
     * set up recycler view with adapter and on click listener
     * also set up swipe and drag listeners
     */
    private fun setupRecyclerView() {
        viewModel.getUserStockList().observe(this,
                Observer {
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
                                    startActivity(StockDetailsActivity.newIntent(context, it.symbol, it.company))
                                    // ActivityUtil.addFragmentToActivity(fragmentManager!!, StockDetailFragment.newInstance(it.symbol), R.id.fragment)
                                }
                            }
                        }
                    }
                    // set up drag and swipe listener
                    val callback = RecyclerViewItemTouchHelper(object: ItemTouchHelperAdapter {
                        /**
                         * recycler view on item dragged
                         */
                        override fun onItemMove(fromPosition: Int, toPosition: Int) {
                            // must call
//                            if(fromPosition < toPosition) {
//                                for (i in fromPosition until toPosition) {
//                                    Collections.swap(viewModel.userStocksList.value, i, i + 1)
//                                }
//                            } else {
//                                for (i in fromPosition downTo toPosition + 1) {
//                                    Collections.swap(viewModel.userStocksList.value, i, i - 1)
//                                }
//                            }
//                            dataBinding.stocksFavRecyclerView.adapter.notifyItemMoved(fromPosition, toPosition)
                            context?.toast("Drag feature is not supported yet.")
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
        // set fixed size to false
        dataBinding.stocksFavRecyclerView.setHasFixedSize(false)
        // hide refresh
        if(dataBinding.swiperefresh.isRefreshing) {
            dataBinding.swiperefresh.isRefreshing = false
        }
    }
}