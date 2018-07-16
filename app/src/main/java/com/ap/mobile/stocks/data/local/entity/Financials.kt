package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Financials(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("financials")
	val financials: List<FinancialsItem?>? = null
)