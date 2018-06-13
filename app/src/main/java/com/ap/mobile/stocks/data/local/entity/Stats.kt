package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName

data class Stats(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("ytdChangePercent")
	val ytdChangePercent: Double? = null,

	@field:SerializedName("month1ChangePercent")
	val month1ChangePercent: Double? = null,

	@field:SerializedName("month3ChangePercent")
	val month3ChangePercent: Double? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("shortInterest")
	val shortInterest: Int? = null,

	@field:SerializedName("EBITDA")
	val eBITDA: Long? = null,

	@field:SerializedName("float")
	val jsonMemberFloat: Long? = null,

	@field:SerializedName("dividendYield")
	val dividendYield: Double? = null,

	@field:SerializedName("revenue")
	val revenue: Long? = null,

	@field:SerializedName("returnOnCapital")
	val returnOnCapital: Any? = null,

	@field:SerializedName("year5ChangePercent")
	val year5ChangePercent: Double? = null,

	@field:SerializedName("cash")
	val cash: Long? = null,

	@field:SerializedName("day30ChangePercent")
	val day30ChangePercent: Double? = null,

	@field:SerializedName("week52high")
	val week52high: Double? = null,

	@field:SerializedName("dividendRate")
	val dividendRate: Double? = null,

	@field:SerializedName("day50MovingAvg")
	val day50MovingAvg: Double? = null,

	@field:SerializedName("peRatioLow")
	val peRatioLow: Double? = null,

	@field:SerializedName("week52low")
	val week52low: Double? = null,

	@field:SerializedName("EPSSurpriseDollar")
	val ePSSurpriseDollar: Any? = null,

	@field:SerializedName("debt")
	val debt: Long? = null,

	@field:SerializedName("marketcap")
	val marketcap: Long? = null,

	@field:SerializedName("month6ChangePercent")
	val month6ChangePercent: Double? = null,

	@field:SerializedName("latestEPS")
	val latestEPS: Double? = null,

	@field:SerializedName("week52change")
	val week52change: Double? = null,

	@field:SerializedName("numberOfEstimates")
	val numberOfEstimates: Int? = null,

	@field:SerializedName("day5ChangePercent")
	val day5ChangePercent: Double? = null,

	@field:SerializedName("latestEPSDate")
	val latestEPSDate: String? = null,

	@field:SerializedName("institutionPercent")
	val institutionPercent: Double? = null,

	@field:SerializedName("profitMargin")
	val profitMargin: Double? = null,

	@field:SerializedName("revenuePerEmployee")
	val revenuePerEmployee: Int? = null,

	@field:SerializedName("peRatioHigh")
	val peRatioHigh: Double? = null,

	@field:SerializedName("sharesOutstanding")
	val sharesOutstanding: Long? = null,

	@field:SerializedName("shortDate")
	val shortDate: String? = null,

	@field:SerializedName("beta")
	val beta: Double? = null,

	@field:SerializedName("grossProfit")
	val grossProfit: Long? = null,

	@field:SerializedName("consensusEPS")
	val consensusEPS: Double? = null,

	@field:SerializedName("EPSSurprisePercent")
	val ePSSurprisePercent: Double? = null,

	@field:SerializedName("exDividendDate")
	val exDividendDate: String? = null,

	@field:SerializedName("returnOnAssets")
	val returnOnAssets: Double? = null,

	@field:SerializedName("day200MovingAvg")
	val day200MovingAvg: Double? = null,

	@field:SerializedName("year2ChangePercent")
	val year2ChangePercent: Double? = null,

	@field:SerializedName("year1ChangePercent")
	val year1ChangePercent: Double? = null,

	@field:SerializedName("shortRatio")
	val shortRatio: Double? = null,

	@field:SerializedName("returnOnEquity")
	val returnOnEquity: Double? = null,

	@field:SerializedName("priceToSales")
	val priceToSales: Double? = null,

	@field:SerializedName("insiderPercent")
	val insiderPercent: Any? = null,

	@field:SerializedName("revenuePerShare")
	val revenuePerShare: Int? = null,

	@field:SerializedName("ttmEPS")
	val ttmEPS: Double? = null,

	@field:SerializedName("priceToBook")
	val priceToBook: Any? = null
)