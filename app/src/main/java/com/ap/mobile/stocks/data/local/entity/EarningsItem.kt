package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName

data class EarningsItem(

	@field:SerializedName("consensusEPS")
	val consensusEPS: Double? = null,

	@field:SerializedName("symbolId")
	val symbolId: Int? = null,

	@field:SerializedName("yearAgoChangePercent")
	val yearAgoChangePercent: Double? = null,

	@field:SerializedName("EPSReportDate")
	val ePSReportDate: String? = null,

	@field:SerializedName("fiscalEndDate")
	val fiscalEndDate: String? = null,

	@field:SerializedName("numberOfEstimates")
	val numberOfEstimates: Int? = null,

	@field:SerializedName("announceTime")
	val announceTime: String? = null,

	@field:SerializedName("fiscalPeriod")
	val fiscalPeriod: String? = null,

	@field:SerializedName("estimatedChangePercent")
	val estimatedChangePercent: Double? = null,

	@field:SerializedName("estimatedEPS")
	val estimatedEPS: Double? = null,

	@field:SerializedName("yearAgo")
	val yearAgo: Double? = null,

	@field:SerializedName("EPSSurpriseDollar")
	val ePSSurpriseDollar: Double? = null,

	@field:SerializedName("actualEPS")
	val actualEPS: Double? = null
)