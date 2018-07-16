package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Earnings(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("earnings")
	val earnings: List<EarningsItem?>? = null
)