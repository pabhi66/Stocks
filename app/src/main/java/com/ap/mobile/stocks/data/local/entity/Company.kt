package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName

data class Company(

	@field:SerializedName("issueType")
	val issueType: String? = null,

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("exchange")
	val exchange: String? = null,

	@field:SerializedName("industry")
	val industry: String? = null,

	@field:SerializedName("CEO")
	val cEO: String? = null,

	@field:SerializedName("sector")
	val sector: String? = null
)