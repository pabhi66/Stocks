package com.ap.mobil.stocks.dagger.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ap.mobil.stocks.VMFactory
import com.ap.mobil.stocks.dagger.scopes.ViewModelKey
import com.ap.mobil.stocks.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/2/18.
 *
 * this will inject/binds all the view models in the app
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsViewModelFactory(vmFactory: VMFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(MainViewModel: MainViewModel): ViewModel
}