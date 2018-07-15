package com.ap.mobile.stocks.data.local.entity

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*



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
	fun getFormattedDate(): String {
		val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-HH:mm", Locale.US)
		val date = sdf.parse(datetime)
		val output = SimpleDateFormat("MM-dd-yyyy", Locale.US)
		return output.format(date)
	}
}