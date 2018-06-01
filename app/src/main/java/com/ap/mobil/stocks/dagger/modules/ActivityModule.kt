package com.ap.mobil.stocks.dagger.modules

import com.ap.mobil.stocks.ui.main.MainActivity
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
}