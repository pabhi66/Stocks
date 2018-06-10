package com.ap.mobile.stocks.data.repository

import com.ap.mobile.stocks.data.local.dao.UserStockListDao
import com.ap.mobile.stocks.data.local.entity.UserStockList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *@author Abhishek Prajapati
 *@since 5/21/18
 *
 * class information...
 */
class UserStockListRepository @Inject constructor(private val userStockList: UserStockListDao) {

    fun getUserStockList(): Single<List<UserStockList>> {
        return userStockList.getAllStock()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun insertStock(stock: UserStockList) {
        Single.fromCallable {
            userStockList.insertStock(stock)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun deleteSingleStock(symbol: String) {
        Single.fromCallable {
            userStockList.deleteSingleStockFromUserStockList(symbol.toUpperCase())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun deleteUserStockList() {
        Single.fromCallable {
            userStockList.deleteAllUserStocks()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}