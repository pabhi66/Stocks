package com.ap.mobile.stocks.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ap.mobile.stocks.di.VMFactory
import com.ap.mobile.stocks.di.scopes.ViewModelKey
import com.ap.mobile.stocks.ui.main.NetworkViewModel
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
    @ViewModelKey(NetworkViewModel::class)
    abstract fun bindsNetworkViewModel(networkViewModel: NetworkViewModel): ViewModel
}