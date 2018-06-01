package com.ap.mobil.stocks.data.repository

import com.ap.mobil.stocks.data.local.entity.Stock
import com.ap.mobil.stocks.data.remote.StockApiService
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
            null
        }.doOnSuccess {

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}