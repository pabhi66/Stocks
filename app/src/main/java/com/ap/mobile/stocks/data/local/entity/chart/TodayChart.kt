package com.ap.mobile.stocks.data.local.entity.chart

import com.google.gson.annotations.SerializedName

data class TodayChart(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("average")
	val average: Double? = null,

	@field:SerializedName("marketAverage")
	val marketAverage: Double? = null,

	@field:SerializedName("notional")
	val notional: Double? = null,

	@field:SerializedName("marketNotional")
	val marketNotional: Double? = null,

	@field:SerializedName("marketVolume")
	val marketVolume: Int? = null,

	@field:SerializedName("marketNumberOfTrades")
	val marketNumberOfTrades: Int? = null,

	@field:SerializedName("marketClose")
	val marketClose: Double? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("marketHigh")
	val marketHigh: Double? = null,

	@field:SerializedName("minute")
	val minute: String? = null,

	@field:SerializedName("marketLow")
	val marketLow: Double? = null,

	@field:SerializedName("volume")
	val volume: Int? = null,

	@field:SerializedName("high")
	val high: Double? = null,

	@field:SerializedName("marketOpen")
	val marketOpen: Double? = null,

	@field:SerializedName("low")
	val low: Double? = null,

	@field:SerializedName("changeOverTime")
	val changeOverTime: Double? = null,

	@field:SerializedName("numberOfTrades")
	val numberOfTrades: Int? = null,

	@field:SerializedName("marketChangeOverTime")
	val marketChangeOverTime: Double? = null,

	@field:SerializedName("close")
	val close: Double? = null,

	@field:SerializedName("open")
	val open: Double? = null
)