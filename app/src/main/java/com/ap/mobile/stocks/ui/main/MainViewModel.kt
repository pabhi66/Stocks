package com.ap.mobile.stocks.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.UserStockList
import com.ap.mobile.stocks.data.repository.StocksRepository
import com.ap.mobile.stocks.data.repository.UserStockListRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val stocksRepository: StocksRepository,
        private val userStockListRepository: UserStockListRepository
): ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
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

    private val userStockListLiveList = MutableLiveData<List<UserStockList>>()
    val userStocksList: LiveData<List<UserStockList>>
        get() = userStockListLiveList

    fun insertStockToUserList(stock: UserStockList) {
        stock.symbol = stock.symbol.toUpperCase()
        userStockListRepository.insertStock(stock)
        getUserStockList()
    }

    fun getUserStockList(): LiveData<List<UserStockList>> {
        disposable.add(userStockListRepository.getUserStockList()
                .subscribe{
                    response -> userStockListLiveList.value = response
                })
        return userStocksList
    }

    fun deleteUserStockList() {
        userStockListRepository.deleteUserStockList()
    }

    fun deleteStock(symbol: String) {
        userStockListRepository.deleteSingleStock(symbol.toUpperCase())
    }


}