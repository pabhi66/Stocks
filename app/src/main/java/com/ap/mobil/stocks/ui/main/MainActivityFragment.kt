package com.ap.mobil.stocks.ui.main

import com.ap.mobil.stocks.R
import com.ap.mobil.stocks.databinding.FragmentMainBinding
import com.ap.mobil.stocks.ui.base.BaseFragment

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.fragment_main
}
