package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName

data class Symbol(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("iexId")
    val iexId: String? = null,

    @field:SerializedName("symbol")
    val symbol: String? = null,

    @field:SerializedName("isEnabled")
    val isEnabled: Boolean? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("type")
    val type: String? = null
)