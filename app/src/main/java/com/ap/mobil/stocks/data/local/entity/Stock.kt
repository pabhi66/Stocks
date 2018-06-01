package com.ap.mobil.stocks.data.local.entity

data class Stock(
	val news: List<News?>? = null,
	val quote: Quote? = null,
	val company: Company? = null
)
