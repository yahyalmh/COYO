package com.coyo.codechallenge.util

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Singleton
import kotlin.math.ceil


@Singleton
class AndroidUtils {

    companion object {
        fun dp(context: Context, value: Float): Int {
            val density = context.resources.displayMetrics.density;

            return if (value == 0f) {
                0
            } else ceil(density * value).toInt()
        }

        @Suppress("DEPRECATION")
        fun isInternetAvailable(context: Context): Boolean {
            try {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && (netInfo.isConnectedOrConnecting || netInfo.isAvailable)) {
                    return true
                }
                netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                if (netInfo != null && netInfo.isConnectedOrConnecting) {
                    return true
                } else {
                    netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    if (netInfo != null && netInfo.isConnectedOrConnecting) {
                        return true
                    }
                }
            } catch (e: Exception) {
                return true
            }
            return false
        }

    }
}