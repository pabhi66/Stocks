package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Stock(

        @field:SerializedName("news")
	val news: List<NewsItem?>? = null,

        @field:SerializedName("financials")
	val financials: Financials? = null,

        @field:SerializedName("earnings")
	val earnings: Earnings? = null,

        @field:SerializedName("quote")
	val quote: Quote? = null,

        @field:SerializedName("stats")
	val stats: Stats? = null,

        @field:SerializedName("company")
	val company: Company? = null
)