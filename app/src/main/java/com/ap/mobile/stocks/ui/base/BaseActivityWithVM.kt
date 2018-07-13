package com.ap.mobile.stocks.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivityWithVM<VM : ViewModel, DB: ViewDataBinding>: AppCompatActivity(), HasSupportFragmentInjector {

    // android injector
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    // view model factory instance
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // data binding instance
    lateinit var dataBinding: DB

    // view model instance
    lateinit var viewModel: VM

    /**
     * get view model
     */
    abstract fun getViewModel(): Class<VM>

    /**
     * get activity layout
     */
    @LayoutRes
    abstract fun getLayoutRes(): Int

    /**
     * get menu
     */
    @MenuRes
    open fun getMenuRes(): Int = 0

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(getMenuRes(), menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
    }

    //fragment injector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}