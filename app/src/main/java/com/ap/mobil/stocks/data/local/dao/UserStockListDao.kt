package com.ap.mobil.stocks.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.ap.mobil.stocks.data.local.entity.UserStockList
import io.reactivex.Single

/**
 *@author Abhishek Prajapati
 *@since 5/21/18
 *
 * class information...
 */

@Dao
interface UserStockListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStock(stock: UserStockList)

    @Query("SELECT * FROM userStockList")
    fun getAllStock(): Single<List<UserStockList>>

    @Query("DELETE FROM userStockList")
    fun deleteAllUserStocks()

    @Query("DELETE FROM userStockList WHERE symbol = :symbol")
    fun deleteSingleStockFromUserStockList(symbol: String)
}