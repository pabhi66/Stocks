package com.ap.mobile.stocks.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.ap.mobile.stocks.util.createParcel
import org.jetbrains.annotations.NotNull

/**
 *@author Abhishek Prajapati
 *@since 5/21/18
 *
 * class information...
 */
@Entity(tableName = "userStockList", indices = [(Index(value = ["symbol"], unique = true))])
data class UserStockList (
    @PrimaryKey(autoGenerate = true)
    val uid: Long,

    @NotNull
    @ColumnInfo(name = "company")
    val company: String = "",

    @NotNull
    @ColumnInfo(name = "symbol")
    var symbol: String = "",

    var price: String = "",

    var priceColor: Int = 0

): Parcelable {
    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { UserStockList(it) }
    }

    private constructor(parcelIn: Parcel) : this (
            parcelIn.readLong(),
            parcelIn.readString(),
            parcelIn.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        uid.let { dest?.writeLong(it) }
        company.let { dest?.writeString(it) }
        symbol.let { dest?.writeString(it) }
    }

    override fun describeContents(): Int = 0
}