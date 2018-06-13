package com.ap.mobile.stocks.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.data.local.entity.chart.TodayChart
import com.ap.mobile.stocks.data.repository.StocksRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 *
 * class information...
 */
class StockDetailViewModel @Inject constructor(
    private val stocksRepository: StocksRepository
): ViewModel() {

    companion object {
        private val TAG = StockDetailViewModel::class.java.simpleName
    }

    private var disposable = CompositeDisposable()
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

    private val stockLiveChart = MutableLiveData<Chart>()

    private val stockChartData: LiveData<Chart>
        get() = stockLiveChart

    fun getChart(symbol: String, range: String): LiveData<Chart> {
        disposable.add(stocksRepository.getChart(symbol, range)
            .subscribe {
                response , error -> stockLiveChart.value = response
                if(error != null) Log.e(TAG, "unable to get chart: $error")
            })
        return stockChartData
    }

    private val stockLiveChartToday = MutableLiveData<TodayChart>()

    private val stockChartDataToday: LiveData<TodayChart>
        get() = stockLiveChartToday

    fun getTodayChart(symbol: String): LiveData<TodayChart> {
        disposable.add(stocksRepository.getTodayChart(symbol)
            .subscribe {
                response , error -> stockLiveChartToday.value = response
                if(error != null) Log.e(TAG, "unable to get chart: $error")
            })
        return stockChartDataToday
    }
}