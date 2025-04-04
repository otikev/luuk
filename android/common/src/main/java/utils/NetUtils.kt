package utils

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
        const val SYSTEM_ERROR = "System Error"

        const val MOCK_SERVER_URL = "https://c93d2298-b08b-4518-9400-a74ede1a39c7.mock.pstmn.io"

        private val TAG = NetUtils::class.java.simpleName
        private var context: Context? = null

        public fun setContext(context: Context?) {
            Companion.context = context
        }

        val isNetworkAvailable: Boolean
            get() {
                var result = false
                return try {
                    result = false
                    val manager =
                        context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val info = manager.activeNetworkInfo
                    result = if (info != null && info.isAvailable) {
                        info.isConnected
                    } else {
                        LogUtils(javaClass).w(
                            "ConnectivityManager says that network is NOT available"
                        )
                        false
                    }
                    result
                } catch (e: Exception) {
                    LogUtils(javaClass).e(e)
                    result
                }
            }
    }
}



