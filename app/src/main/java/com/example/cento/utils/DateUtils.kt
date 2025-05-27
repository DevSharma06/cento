package com.example.cento.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    fun convertIntoDate(timestampInMillis: Long): String {
        val now = Calendar.getInstance()
        val inputDate = Calendar.getInstance().apply {
            timeInMillis = timestampInMillis
        }

        return when {
            isSameDay(now, inputDate) -> "Today"
            isSameDay(now.apply { add(Calendar.DATE, -1) }, inputDate) -> "Yesterday"
            else -> {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                sdf.format(Date(timestampInMillis)).toString()
            }
        }
    }

    fun filterExpenseByTimeInterval(timeInterval: TimeInterval, timestampInMillis: Long): Boolean {
        val now = Calendar.getInstance()
        val inputDate = Calendar.getInstance().apply {
            timeInMillis = timestampInMillis
        }
        return when (timeInterval) {
            TimeInterval.WEEK -> isSameWeek(now, inputDate)
            TimeInterval.MONTH -> isSameMonth(now, inputDate)
            TimeInterval.YEAR -> isSameYear(now, inputDate)
            else -> isSameDay(now, inputDate)
        }
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
    }

    private fun isSameMonth(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
    }

    private fun isSameYear(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }
}

enum class TimeInterval {
    DAY, WEEK, MONTH, YEAR
}