package com.ap.mobil.stocks.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.ap.mobil.stocks.R
import com.ap.mobil.stocks.data.local.entity.UserStockList
import com.ap.mobil.stocks.databinding.FragmentMainBinding
import com.ap.mobil.stocks.ui.base.BaseFragment

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    companion object {
        private val TAG = MainActivityFragment::class.java.simpleName
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel.getStockData("aapl")
//            .observe(this, Observer {
//                println(it?.company?.companyName)
//                println(it?.news?.get(0)?.headline)
//            })



        viewModel.getUserStockList()
                .observe(this, Observer {
                   it?.forEach {
                       println("${it.symbol}, ${it.company}")
                   }
                })
    }
}
