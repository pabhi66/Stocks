package com.ap.mobile.stocks.data.local.entity

import com.ap.mobile.stocks.util.format
import com.google.gson.annotations.SerializedName

data class List(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("avgTotalVolume")
	val avgTotalVolume: Int? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("iexRealtimePrice")
	val iexRealtimePrice: Double? = null,

	@field:SerializedName("delayedPrice")
	val delayedPrice: Double? = null,

	@field:SerializedName("iexMarketPercent")
	val iexMarketPercent: Double? = null,

	@field:SerializedName("calculationPrice")
	val calculationPrice: String? = null,

	@field:SerializedName("extendedChangePercent")
	val extendedChangePercent: Double? = null,

	@field:SerializedName("latestSource")
	val latestSource: String? = null,

	@field:SerializedName("primaryExchange")
	val primaryExchange: String? = null,

	@field:SerializedName("latestUpdate")
	val latestUpdate: Long? = null,

	@field:SerializedName("iexBidPrice")
	val iexBidPrice: Double? = null,

	@field:SerializedName("previousClose")
	val previousClose: Double? = null,

	@field:SerializedName("high")
	val high: Double? = null,

	@field:SerializedName("peRatio")
	val peRatio: Double? = null,

	@field:SerializedName("low")
	val low: Double? = null,

	@field:SerializedName("delayedPriceTime")
	val delayedPriceTime: Long? = null,

	@field:SerializedName("extendedPrice")
	val extendedPrice: Double? = null,

	@field:SerializedName("extendedPriceTime")
	val extendedPriceTime: Long? = null,

	@field:SerializedName("week52Low")
	val week52Low: Double? = null,

	@field:SerializedName("closeTime")
	val closeTime: Long? = null,

	@field:SerializedName("changePercent")
	val changePercent: Double? = null,

	@field:SerializedName("week52High")
	val week52High: Double? = null,

	@field:SerializedName("openTime")
	val openTime: Long? = null,

	@field:SerializedName("sector")
	val sector: String? = null,

	@field:SerializedName("close")
	val close: Double? = null,

	@field:SerializedName("latestPrice")
	val latestPrice: Double? = null,

	@field:SerializedName("marketCap")
	val marketCap: Long? = null,

	@field:SerializedName("iexRealtimeSize")
	val iexRealtimeSize: Int? = null,

	@field:SerializedName("iexLastUpdated")
	val iexLastUpdated: Long? = null,

	@field:SerializedName("change")
	val change: Double? = null,

	@field:SerializedName("latestVolume")
	val latestVolume: Int? = null,

	@field:SerializedName("iexAskPrice")
	val iexAskPrice: Double? = null,

	@field:SerializedName("ytdChange")
	val ytdChange: Double? = null,

	@field:SerializedName("iexVolume")
	val iexVolume: Int? = null,

	@field:SerializedName("iexAskSize")
	val iexAskSize: Int? = null,

	@field:SerializedName("extendedChange")
	val extendedChange: Double? = null,

	@field:SerializedName("latestTime")
	val latestTime: String? = null,

	@field:SerializedName("open")
	val open: Double? = null,

	@field:SerializedName("iexBidSize")
	val iexBidSize: Int? = null
) {
	fun getFormatedPrice(): String {
		if(iexRealtimePrice == null) return "-"
		return "$" + iexRealtimePrice.format(iexRealtimePrice)
	}

	fun getFormatedChange(): String {
		if(changePercent == null) return "-"
		return changePercent.format(changePercent) + "%"
	}

	fun getPriceColor(): Int {
		if(changePercent == null) return 0
		return if(changePercent < 0) -1
		else 1
	}
}