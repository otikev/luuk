package com.luuk.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by kevin on 1/6/2017.
 */
@SuppressLint("StaticFieldLeak")
class NetUtils {
    companion object {
        const val CONNECTIVITY_ERROR_CODE: Int = -2
        const val NETWORK_ERROR_CODE: Int = -1
        const val UNAUTHORIZED_ERROR_CODE: Int = 401
        const val NETWORK_CONNECTION_OFF = "Network Error"
        const val SYSTEM_ERROR  = "System Error"

        const val MOCK_SERVER_URL = "https://c93d2298-b08b-4518-9400-a74ede1a39c7.mock.pstmn.io"
        const val LOCAL_TEST_URL = "http://192.168.0.109:8080/"
        private val TAG = NetUtils::class.java.simpleName
        private var context: Context? = null

        public fun setContext(context: Context?) {
            NetUtils.context = context
        }

        val isNetworkAvailable: Boolean
            get() {
                var result = false
                return try {
                    result = false
                    val manager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val info = manager.activeNetworkInfo
                    result = if (info != null && info.isAvailable) {
                        info.isConnected
                    } else {
                        LogUtils.logW(TAG, "ConnectivityManager says that network is NOT available")
                        false
                    }
                    result
                } catch (e: Exception) {
                    LogUtils.logE(TAG, e.message, e)
                    result
                }
            }
    }
}



