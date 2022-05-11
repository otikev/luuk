package utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val TAG = DateUtils::class.java.simpleName
    val inventoryDateFormatFromServer="yyyy-MM-dd HH:mm:ss"
    val dateFormatStr = "dd/MM/yyyy"
    val filterDateFormatStr="yyyy-MM-dd"
    val swappedDateFormatStr = "dd/MM/yyyy hh:mma"
    val timeFormat = SimpleDateFormat("hh:mma", Locale.ENGLISH)

    fun getDateToday(requiredOutputFormat:String): String {
        val mDateFormat = SimpleDateFormat(requiredOutputFormat, Locale.ENGLISH)
        return mDateFormat.format(Calendar.getInstance().time)
    }

    fun getTimeNow(): String {
        return timeFormat.format(Calendar.getInstance().time)
    }

    fun formatDateString(dateStr:String?, providedDateInFormat: String, requiredOutputFormat:String):
            String? {
        val dateObject= getDateObject(dateStr,providedDateInFormat)
        if(dateObject!=null){
            return formatDate(dateObject, requiredOutputFormat)
        }
       return dateStr
    }

    fun formatDate(date:Date, requiredOutputFormat:String):
            String {
        val mDateFormat = SimpleDateFormat(requiredOutputFormat, Locale.ENGLISH)
        return mDateFormat.format(date)
    }

    private fun getDateObject(target: String?, providedDateInFormat: String?): Date? {
        if (target == null || providedDateInFormat == null) return null
        try {
            val mDateFormat = SimpleDateFormat(providedDateInFormat, Locale.ENGLISH)
            mDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return mDateFormat.parse(target)
        } catch (e: ParseException) {
            LogUtils(javaClass).e(e)
        }
        return null
    }

    fun getSubtractedDate(date: Date, numDays: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -numDays)
        return calendar.time
    }

    fun getAddedDate(date: Date, numDays: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.DATE, numDays)
        return calendar.time
    }

}