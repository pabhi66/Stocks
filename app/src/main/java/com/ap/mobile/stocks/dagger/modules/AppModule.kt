package com.ap.mobile.stocks.dagger.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.ap.mobile.stocks.data.local.dao.UserStockListDao
import com.ap.mobile.stocks.data.local.database.UserStockListDatabase
import com.ap.mobile.stocks.data.remote.StockApiService
import com.facebook.stetho.okhttp3.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/2/18.
 *
 * this class has all the dependencies that we need through the lifecycle of our app
 * such as retrofit api service, http client, database dao, room database, etc.
 */

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    companion object {
        private const val TAG = "AppModule"
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(StethoInterceptor())
                addInterceptor(httpLoggingInterceptor)
            }
        }
        return okHttpClient.build()
    }

    // =============== Stocks Data ====================
    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): StockApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.iextrading.com/1.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(StockApiService::class.java)
    }

    // =============== User Stocks List ====================
    @Provides
    @Singleton
    fun provideUserStockDatabase(application: Application): UserStockListDatabase =
            Room.databaseBuilder(application, UserStockListDatabase::class.java, "userStocks.db")
                    .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserStockListDao(userStockListDatabase: UserStockListDatabase): UserStockListDao =
            userStockListDatabase.userStockListDao()

}