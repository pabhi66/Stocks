package com.ap.mobile.stocks.di.modules

import com.ap.mobile.stocks.ui.main.MainActivity
import com.ap.mobile.stocks.ui.detail.StockDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/2/18.
 *
 * this class will include all activities in our app
 */

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [(FragmentModule::class)])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [(FragmentModule::class)])
    abstract fun detailsActivity(): StockDetailsActivity

}