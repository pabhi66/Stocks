package com.ap.mobil.stocks.data.remote

import com.ap.mobil.stocks.data.local.entity.Stock
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *@author Abhishek Prajapati
 *@since 5/20/18
 *
 * This is an interface which indicate how a http request will be handled
 */
interface StockApiService {

    @GET("stock/{symbol}/batch?types=quote,news,company&last=10")
    fun getStockData(@Path("symbol") symbol: String): Single<Stock>
}