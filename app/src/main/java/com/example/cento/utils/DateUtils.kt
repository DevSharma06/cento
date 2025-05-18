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

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}