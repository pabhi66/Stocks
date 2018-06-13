package com.ap.mobile.stocks.data.local.entity.chart

import com.google.gson.annotations.SerializedName

data class Chart(

	val date: String? = null,

	val volume: Int? = null,

	val high: Double? = null,

	val unadjustedVolume: Int? = null,

	val low: Double? = null,

	val changeOverTime: Int? = null,

	val change: Double? = null,

	val vwap: Double? = null,

	val changePercent: Double? = null,

	val label: String? = null,

	val close: Double? = null,

	val open: Double? = null
)