package com.example.appmoney.ui.common.helper

import android.util.Log
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeHelper {

    private val calendar = Calendar.getInstance()
    private val locale = Locale.getDefault()


    fun getByFormat(cal: Calendar, format: TimeFormat): String {
        return try {
                SimpleDateFormat(format.formatString, locale).format(cal.time)
        } catch (e: IllegalArgumentException) {
            Log.e(this.javaClass.name, "err: $e")
            ""
        }
    }

    fun stringToDate(dateString: String): Date {
        val sdf = SimpleDateFormat(TimeFormat.Date.formatString, Locale.getDefault())
        return sdf.parse(dateString) ?: throw IllegalArgumentException("Không thể parse ngày từ chuỗi")
    }

    fun stringToTimestamp( dateString: String): Timestamp?{
        return try {
            val sfd = SimpleDateFormat(TimeFormat.Date.formatString, locale)
            val date = sfd.parse(dateString)
            if (date!= null) Timestamp(date) else null
        }catch (e : Exception){
            Log.e(this.javaClass.name, "err: $e")
            null
        }
    }

    fun timestampToString( timestamp: Timestamp): String{
        return try {
            val sfd = SimpleDateFormat(TimeFormat.Date.formatString, locale)
            sfd.format(timestamp.toDate())
        }catch (e: Exception){
            Log.e(this.javaClass.name,"err: $e")
            ""
        }
    }

    fun getStartOfMonth(date: Date): Timestamp {
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return Timestamp(calendar.time)
    }

    fun getEndOfMonth(date: Date): Timestamp {
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return Timestamp(calendar.time)
    }

    fun getStartOfYear(date: Date): Timestamp {
        calendar.time = date
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return Timestamp(calendar.time)
    }

    fun getEndOfYear(date: Date): Timestamp {
        calendar.time = date
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return Timestamp(calendar.time)
    }

}

enum class TimeFormat(val formatString: String) {
    Time("HH:mm:ss"),
    Date("EEE,dd/MM/yyyy"),
    MonthDate("MM/yyyy"),
    DateTime("dd/MM/yyyy hh:mm:ss")

}