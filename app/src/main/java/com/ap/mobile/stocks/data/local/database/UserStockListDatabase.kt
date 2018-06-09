package com.ap.mobile.stocks.data.local.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.ap.mobile.stocks.data.local.dao.UserStockListDao
import com.ap.mobile.stocks.data.local.entity.UserStockList

/**
 *@author Abhishek Prajapati
 *@since 5/21/18
 *
 * class information...
 */

@Database(entities = [(UserStockList::class)], version = 1, exportSchema = false)
abstract class UserStockListDatabase: RoomDatabase() {
    abstract fun userStockListDao(): UserStockListDao
}