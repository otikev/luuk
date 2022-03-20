package utils

import android.util.Log

object LogUtils {
    private const val LOG_PREFIX = "LUUK-"
    private const val LOG_PREFIX_LENGTH = LOG_PREFIX.length
    private const val MAX_LOG_TAG_LENGTH = 30

    private fun makeLogTag(str: String): String {
        return if (str.length > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            LOG_PREFIX + str.substring(
                0,
                MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1
            )
        } else LOG_PREFIX + str
    }

    /**
     * Debug logs, intended for Logcat only
     */
    fun logD(tag: String, message: String) {
        try {
            Log.d(makeLogTag(tag), message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Verbose logs, intended for Logcat only
     */
    fun logV(tag: String, message: String) {
        try {
            Log.v(makeLogTag(tag), message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Informative logs, will be sent to remote systems
     */
    fun logI(tag: String, message: String, shouldLogUserId: Boolean = true) {
            Log.i(makeLogTag(tag), message)
    }

    /**
     * Warning logs, will be sent to remote systems
     */
    fun logW(tag: String, message: String, shouldLogUserId: Boolean = true) {
        Log.w(makeLogTag(tag), message)
    }

    /**
     * Error logs, will be sent to remote systems
     */
    fun logE(tag: String, message: String, shouldLogUserId: Boolean = true) {
        Log.e(makeLogTag(tag), message)
    }

    /**
     * Error logs, will be sent to remote systems
     */
    fun logE(tag: String, message: String?, cause: Throwable, shouldLogUserId: Boolean = true) {
            Log.e(makeLogTag(tag), message, cause)
    }

}