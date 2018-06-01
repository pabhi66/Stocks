package com.ap.mobil.stocks.ui.base

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

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/2/18.
 *
 * This is a base class of activity which will extends all activities in the app
 */
abstract class BaseActivity<DB: ViewDataBinding>: AppCompatActivity(), HasSupportFragmentInjector {

    // android injector
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    // data binding instance
    lateinit var dataBinding: DB

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

    /**
     * override on create. Inject this activity and bind the layout
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
    }

    // fragment injector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}