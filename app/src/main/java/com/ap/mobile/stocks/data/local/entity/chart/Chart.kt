package com.ap.mobile.stocks.data.local.entity.chart

import com.google.gson.annotations.SerializedName

data class Chart(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("volume")
	val volume: Int? = null,

	@field:SerializedName("high")
	val high: Double? = null,

	@field:SerializedName("unadjustedVolume")
	val unadjustedVolume: Int? = null,

	@field:SerializedName("low")
	val low: Double? = null,

	@field:SerializedName("changeOverTime")
	val changeOverTime: Double? = null,

	@field:SerializedName("change")
	val change: Double? = null,

	@field:SerializedName("vwap")
	val vwap: Double? = null,

	@field:SerializedName("changePercent")
	val changePercent: Double? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("close")
	val close: Double? = null,

	@field:SerializedName("open")
	val open: Double? = null
)