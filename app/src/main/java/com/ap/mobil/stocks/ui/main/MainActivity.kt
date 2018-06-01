package com.ap.mobil.stocks.ui.main

import android.os.Bundle
import com.ap.mobil.stocks.R
import com.ap.mobil.stocks.databinding.ActivityMainBinding
import com.ap.mobil.stocks.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dataBinding.toolbar)
    }

    override fun getMenuRes(): Int = R.menu.menu_main
}
