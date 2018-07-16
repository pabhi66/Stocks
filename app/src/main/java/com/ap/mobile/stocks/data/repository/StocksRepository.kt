package com.ap.mobile.stocks.data.repository

import com.ap.mobile.stocks.data.local.entity.NewsItem
import com.ap.mobile.stocks.data.local.entity.Quote
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.Symbol
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.data.local.entity.chart.TodayChart
import com.ap.mobile.stocks.data.remote.StockApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
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

    fun getTodayChart(symbol: String): Single<List<TodayChart>> {
        return stocksApiService.getTodayChart(symbol).onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getChart(symbol: String, range: String): Single<List<Chart>> {
        return stocksApiService.getChart(symbol, range).onErrorResumeNext {
            it.printStackTrace()
            null
        }
            .doOnSuccess {  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSymbols() : Single<List<Symbol>> {
        return stocksApiService.getSymbols().onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getQuote(symbol: String): Call<Quote> {
        return stocksApiService.getQuote(symbol)
    }

    fun getMostActiveStocks(): Single<List<com.ap.mobile.stocks.data.local.entity.List>> {
        return stocksApiService.getMostActiveStocks().onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getGainersStocks(): Single<List<com.ap.mobile.stocks.data.local.entity.List>> {
        return stocksApiService.getGainersStocks().onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLosersStocks(): Single<List<com.ap.mobile.stocks.data.local.entity.List>> {
        return stocksApiService.getLosersStocks().onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLatestNews(): Single<List<NewsItem>> {
        return stocksApiService.getLatestNews().onErrorResumeNext {
            it.printStackTrace()
            null
        }.doOnSuccess {}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}