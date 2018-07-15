package com.ap.mobile.stocks.data.remote

import com.ap.mobile.stocks.data.local.entity.Quote
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.Symbol
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.data.local.entity.chart.TodayChart
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *@author Abhishek Prajapati
 *@since 5/20/18
 *
 * This is an interface which indicate how a http request will be handled
 */
interface StockApiService {

    @GET("stock/{symbol}/batch?types=quote,news,company,earnings,financials,stats&range=1d&last=5")
    fun getStockData(@Path("symbol") symbol: String): Single<Stock>

    @GET("stock/{symbol}/chart/1d")
    fun getTodayChart(@Path("symbol") symbol: String): Single<List<TodayChart>>

    @GET("stock/{symbol}/chart/{range}")
    fun getChart(@Path("symbol") symbol: String, @Path("range")range:String): Single<List<Chart>>

    @GET("ref-data/symbols")
    fun getSymbols(): Single<List<Symbol>>

    @GET("stock/{symbol}/quote")
    fun getQuote(@Path("symbol") symbol: String): Call<Quote>
}