package com.ap.mobile.stocks.util

import android.content.Context
import android.telephony.TelephonyManager
import android.net.ConnectivityManager
import android.net.NetworkInfo


object NetworkUtil {
    val NOT_CONNECTED = 0
    val WIFI = 1
    val MOBILE = 2

    fun getConnectionStatus(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return MOBILE
        }
        return NOT_CONNECTED
    }
}