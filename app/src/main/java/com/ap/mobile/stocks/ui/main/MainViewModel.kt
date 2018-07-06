package com.ap.mobile.stocks.ui.main

import android.arch.lifecycle.*
import android.os.Handler
import android.util.Log
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.Symbol
import com.ap.mobile.stocks.data.local.entity.UserStockList
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.data.local.entity.chart.TodayChart
import com.ap.mobile.stocks.data.repository.StocksRepository
import com.ap.mobile.stocks.data.repository.UserStockListRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val stocksRepository: StocksRepository,
    private val userStockListRepository: UserStockListRepository
): ViewModel() {


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
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
        Log.e(TAG, symbol)
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
                response, error -> stockSymbolLiveList.value = response
                if(error != null) Log.e(TAG, "unable to get symbols")
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

}