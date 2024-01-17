package com.ycompany.yelectronics.network.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager

object NetworkUtils {
    fun isDeviceOnline(context: Context): Boolean {
        val connManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connManager.getNetworkCapabilities(connManager.activeNetwork)
        return networkCapabilities != null
    }
}