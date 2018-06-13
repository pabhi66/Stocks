package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName

data class FinancialsItem(

	@field:SerializedName("operatingIncome")
	val operatingIncome: Long? = null,

	@field:SerializedName("operatingExpense")
	val operatingExpense: Long? = null,

	@field:SerializedName("currentCash")
	val currentCash: Long? = null,

	@field:SerializedName("currentDebt")
	val currentDebt: Long? = null,

	@field:SerializedName("cashChange")
	val cashChange: Long? = null,

	@field:SerializedName("totalAssets")
	val totalAssets: Long? = null,

	@field:SerializedName("totalDebt")
	val totalDebt: Long? = null,

	@field:SerializedName("totalLiabilities")
	val totalLiabilities: Any? = null,

	@field:SerializedName("cashFlow")
	val cashFlow: Long? = null,

	@field:SerializedName("costOfRevenue")
	val costOfRevenue: Long? = null,

	@field:SerializedName("currentAssets")
	val currentAssets: Long? = null,

	@field:SerializedName("shareholderEquity")
	val shareholderEquity: Long? = null,

	@field:SerializedName("researchAndDevelopment")
	val researchAndDevelopment: Long? = null,

	@field:SerializedName("reportDate")
	val reportDate: String? = null,

	@field:SerializedName("operatingRevenue")
	val operatingRevenue: Long? = null,

	@field:SerializedName("totalCash")
	val totalCash: Long? = null,

	@field:SerializedName("operatingGainsLosses")
	val operatingGainsLosses: Any? = null,

	@field:SerializedName("netIncome")
	val netIncome: Long? = null,

	@field:SerializedName("totalRevenue")
	val totalRevenue: Long? = null,

	@field:SerializedName("grossProfit")
	val grossProfit: Long? = null
)