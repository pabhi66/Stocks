package com.ap.mobile.stocks.data.repository

import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.data.local.entity.chart.TodayChart
import com.ap.mobile.stocks.data.remote.StockApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *@author Abhishek Prajapati
 *@since 5/20/18
 *
 * class information...
 */
class StocksRepository @Inject constructor(private val stocksApiService: StockApiService) {

    fun getStockData(symbol: String): Single<Stock> {
        return stocksApiService.getStockData(symbol).onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTodayChart(symbol: String): Single<TodayChart> {
        return stocksApiService.getTodayChart(symbol).onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getChart(symbol: String, range: String): Single<Chart> {
        return stocksApiService.getChart(symbol, range).onErrorResumeNext {
            it.printStackTrace()
            null
        }
            .doOnSuccess {  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}