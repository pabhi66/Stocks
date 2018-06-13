package com.ap.mobile.stocks.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK



object DimensionUtil {

    fun pxToDp(px: Float): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dip: Float): Int {
        return (dip * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToSp(px: Float): Int {
        return (px / Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }

    fun spToPx(sp: Float): Int {
        return (sp * Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}