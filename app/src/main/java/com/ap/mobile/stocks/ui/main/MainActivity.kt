package com.ap.mobile.stocks.ui.main

import android.os.Bundle
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.ActivityMainBinding
import com.ap.mobile.stocks.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dataBinding.toolbar)
    }

    override fun getMenuRes(): Int = R.menu.menu_main
}
