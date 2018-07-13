package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName

data class NewsItem(

	@field:SerializedName("summary")
	val summary: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("datetime")
	val datetime: String? = null,

	@field:SerializedName("related")
	val related: String? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("headline")
	val headline: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) {
	fun getFormattedDate() {

	}
}