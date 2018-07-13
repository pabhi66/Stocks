package com.ap.mobile.stocks

import android.app.Activity
import android.app.Application
import com.ap.mobile.stocks.di.components.DaggerAppComponent
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        initInjector()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            if (LeakCanary.isInAnalyzerProcess(this)) return
            LeakCanary.install(this)
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    private fun initInjector() {
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

}