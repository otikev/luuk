package utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

object MiscUtils {
    private val TAG = MiscUtils::class.java.simpleName
    var progressDialog: ProgressDialog? = null

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @JvmStatic
    fun showLoader(dialogContext: Context, title: String?, msg: String?) {
        synchronized(MiscUtils::class.java) {
            progressDialog?.dismiss()
            val hostActivity = dialogContext as Activity
            if (!hostActivity.isFinishing) {
                progressDialog = ProgressDialog.show(dialogContext, title, msg, true)
                progressDialog?.setOwnerActivity(hostActivity)
            }
        }
    }

    @JvmStatic
    fun getAutoCapitalizedText(text: String?): String {
        val result = StringBuilder()
        if (text != null) {
            val splitTextArray = removeEmptyStrings(ArrayList(text.split(" ")))

            for (i in splitTextArray.indices) {
                val stringBeingProcessed = splitTextArray[i].trim { it <= ' ' }
                val firstLetter = stringBeingProcessed[0].toString()
                val theRest = stringBeingProcessed.substring(1)
                result.append(firstLetter.toUpperCase(Locale.ENGLISH))
                    .append(theRest.toLowerCase(Locale.ENGLISH))
                    .append(if (i == splitTextArray.size - 1) "" else " ")
            }
        }
        return result.trim(' ').toString()
    }

    @JvmStatic
    fun getFirstname(text: String): String {
        val firstName = text.split(" ")[0]
        return getAutoCapitalizedText(firstName)

    }

    @JvmStatic
    fun getTwoNames(text: String): String {
        val strArray = text.trim(' ').split(" ")
        var twoNames = ""
        if (strArray.size < 2) {
            twoNames = strArray[0]
        } else {
            for (i in 0..1) {
                twoNames += strArray[i] + " "
            }
            twoNames.trim(' ')
        }
        return getAutoCapitalizedText(twoNames)

    }


    private fun removeEmptyStrings(arrayList: ArrayList<String>): ArrayList<String> {
        if (arrayList.contains("")) {
            arrayList.remove("")
            removeEmptyStrings(arrayList)
        }
        return arrayList
    }

    @JvmStatic
    fun dismissLoader() {
        synchronized(MiscUtils::class.java) {
            if (progressDialog != null) {
                val hostActivity = progressDialog!!.ownerActivity
                if (hostActivity != null && !hostActivity.isFinishing && progressDialog!!.isShowing
                    && !hostActivity.isDestroyed
                ) {
                    progressDialog!!.dismiss()
                }
                progressDialog = null
            }
        }
    }


    @JvmStatic
    fun getFormattedAmount(number: Double?, currency: String): String {
        val formatter = DecimalFormat("#,###.##")
        val amountStr = formatter.format(number)

        return String.format("$currency %s", amountStr)
    }


    @JvmStatic
    fun getSpannedText(text: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(text)
        }
    }

    fun getUserNameInitials(userName: String?): String {
        var nameInitials = ""
        if (userName == null) {
            return nameInitials
        }
        val providedInput = userName.trim { it <= ' ' }
        if (providedInput.length > 0) {
            if (providedInput.contains(" ")) {
                val parts = userName.split(" ").toTypedArray()
                for (i in parts.indices) {
                    if (parts[i].length > 0) {
                        val initial = parts[i][0]
                        if (!Character.isWhitespace(initial)) {
                            nameInitials += initial
                            if (nameInitials.length == 2) {
                                break
                            }
                        }
                    }
                }
            } else {
                nameInitials += providedInput[0]
                return nameInitials
            }
        }
        return nameInitials.toUpperCase(Locale.getDefault())
    }

    fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return service.foreground
            }
        }
        return false
    }

    fun isAndroid10PlusDevice(): Boolean {
        return Build.VERSION.SDK_INT >= 29
    }
}