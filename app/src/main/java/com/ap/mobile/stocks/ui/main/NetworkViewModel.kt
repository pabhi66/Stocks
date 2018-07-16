package com.ap.mobile.stocks.ui.main

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.util.Log
import com.ap.mobile.stocks.data.local.entity.NewsItem
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.Symbol
import com.ap.mobile.stocks.data.local.entity.UserStockList
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.data.local.entity.chart.TodayChart
import com.ap.mobile.stocks.data.repository.StocksRepository
import com.ap.mobile.stocks.data.repository.UserStockListRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
        val stocksRepository: StocksRepository,
        private val userStockListRepository: UserStockListRepository
): ViewModel() {

    companion object {
        private val TAG = NetworkViewModel::class.java.simpleName
    }


    //=======================================================
    //                  DISPOSABLE
    //=======================================================
    private var disposable = CompositeDisposable()


    //=======================================================
    //                GET STOCK DATA
    //=======================================================
    private val stockLiveList = MutableLiveData<Stock>()
    private val stockData: LiveData<Stock>
        get() = stockLiveList

    fun getStockData(symbol: String): LiveData<Stock> {
        disposable.add(stocksRepository.getStockData(symbol)
                .subscribe{
                    response, error -> stockLiveList.value = response
                    if(error != null) Log.e(TAG, "symbol: $symbol not found. Error: $error")
                })
        return stockData
    }

    //=======================================================
    //                GET USER FAV STOCK LIST
    //=======================================================
    private val userStockListLiveList = MutableLiveData<List<UserStockList>>()
    val userStocksList: LiveData<List<UserStockList>>
        get() = userStockListLiveList

    fun getUserStockList(): LiveData<List<UserStockList>> {
        disposable.add(userStockListRepository.getUserStockList()
                .subscribe{
                    response -> userStockListLiveList.value = response
                })

        return userStocksList
    }

    //=======================================================
    //                GET STOCK SYMBOLS
    //=======================================================
    private val stockSymbolLiveList = MutableLiveData<List<Symbol>>()
    private val stockSymbolData: LiveData<List<Symbol>>
        get() = stockSymbolLiveList

    fun getStockSymbols(): LiveData<List<Symbol>> {
        disposable.add(stocksRepository.getSymbols()
                .subscribe{
                    response, error ->
                    if(error != null) Log.e(TAG, "unable to get symbols")
                    else stockSymbolLiveList.value = response
                })
        return stockSymbolData
    }

    //=======================================================
    //          INSERT STOCK TO USER FAV STOCK LIST
    //=======================================================
    fun insertStockToUserList(stock: UserStockList) {
        stock.symbol = stock.symbol.toUpperCase()
        userStockListRepository.insertStock(stock)
        val handler = Handler()
        handler.postDelayed({
            getUserStockList()
        }, 100)
    }

    fun insertStockToUserList(symbol: String, owner: LifecycleOwner) {
        getStockData(symbol = symbol).observe(owner, Observer {
            if(it != null) {
                insertStockToUserList(UserStockList(0, it.company?.companyName!!, it.company.symbol!!))
            }
        })
    }

    //=======================================================
    //         DELETE STOCK FROM USER FAV STOCK LIST
    //=======================================================
    fun deleteStock(symbol: String) {
        userStockListRepository.deleteSingleStock(symbol)
        val handler = Handler()
        handler.postDelayed({
            getUserStockList()
        }, 1000)
    }


    //=======================================================
    //         GET STOCK CHART DATA IN SOME RANGE
    //=======================================================
    private val stockLiveChart = MutableLiveData<List<Chart>>()
    private val stockChartData: LiveData<List<Chart>>
        get() = stockLiveChart

    fun getChart(symbol: String, range: String): LiveData<List<Chart>> {
        disposable.add(stocksRepository.getChart(symbol, range)
                .subscribe {
                    response , error -> stockLiveChart.value = response
                    if(error != null) Log.e(TAG, "unable to get chart: $error")
                })
        return stockChartData
    }

    //=======================================================
    //         DELETE STOCK'S TODAY CHART DATA
    //=======================================================
    private val stockLiveChartToday = MutableLiveData<List<TodayChart>>()
    private val stockChartDataToday: LiveData<List<TodayChart>>
        get() = stockLiveChartToday

    fun getTodayChart(symbol: String): LiveData<List<TodayChart>> {
        disposable.add(stocksRepository.getTodayChart(symbol)
                .subscribe {
                    response , error -> stockLiveChartToday.value = response
                    if(error != null) Log.e(TAG, "unable to get chart: $error")
                })
        return stockChartDataToday
    }

    //=======================================================
    //         GET MOST ACTIVE STOCKS
    //=======================================================
    private val mostActiveStocksLive = MutableLiveData<List<com.ap.mobile.stocks.data.local.entity.List>>()
    private val mostActiveStocksData: LiveData<List<com.ap.mobile.stocks.data.local.entity.List>>
        get() = mostActiveStocksLive

    fun getMostActiveStocks(): LiveData<List<com.ap.mobile.stocks.data.local.entity.List>> {
        disposable.add(stocksRepository.getMostActiveStocks().subscribe {
            response, error -> mostActiveStocksLive.value = response
            if(error != null) Log.e(TAG, "unable to get most active stocks: $error")
        })
        return mostActiveStocksData
    }

    //=======================================================
    //         GET TODAY'S GAINERS
    //=======================================================
    private val gainersStocksLive = MutableLiveData<List<com.ap.mobile.stocks.data.local.entity.List>>()
    private val gainersStocksData: LiveData<List<com.ap.mobile.stocks.data.local.entity.List>>
        get() = gainersStocksLive

    fun getGainersStocks(): LiveData<List<com.ap.mobile.stocks.data.local.entity.List>> {
        disposable.add(stocksRepository.getGainersStocks().subscribe {
            response, error -> gainersStocksLive.value = response
            if(error != null) Log.e(TAG, "unable to get gainers: $error")
        })
        return gainersStocksData
    }

    //=======================================================
    //         GET TODAY'S LOSERS
    //=======================================================
    private val losersStocksLive = MutableLiveData<List<com.ap.mobile.stocks.data.local.entity.List>>()
    private val losersStocksData: LiveData<List<com.ap.mobile.stocks.data.local.entity.List>>
        get() = losersStocksLive

    fun getLosersStocks(): LiveData<List<com.ap.mobile.stocks.data.local.entity.List>> {
        disposable.add(stocksRepository.getLosersStocks().subscribe {
            response, error -> losersStocksLive.value = response
            if(error != null) Log.e(TAG, "unable to get losers: $error")
        })
        return losersStocksData
    }

    //=======================================================
    //         GET LATEST NEWS
    //=======================================================
    private val latestNewsLive = MutableLiveData<List<NewsItem>>()
    private val latestNewsData: LiveData<List<NewsItem>>
        get() = latestNewsLive

    fun getLatestNews(): LiveData<List<NewsItem>> {
        disposable.add(stocksRepository.getLatestNews().subscribe {
            response, error -> latestNewsLive.value = response
            if(error != null) Log.e(TAG, "unable to get latest news: $error")
        })
        return latestNewsData
    }

}