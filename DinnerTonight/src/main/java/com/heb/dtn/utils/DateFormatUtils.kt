package com.heb.dtn.utils

//
// Created by Khuong Huynh on 9/27/17.
//

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatUtils {

    // Month/Date/Year
    fun mmddyyyy(): DateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    // 24 hour time only
    fun twentyFourHours(): DateFormat = SimpleDateFormat("kk:mm", Locale.getDefault())

    // Abbreviated day of the week - Tue
    fun dayOfWeekShort(): DateFormat = SimpleDateFormat("EE", Locale.getDefault())

    // Day of the week - Wednesday
    fun dayOfWeek(): DateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    // Abbreviated month and date - Sept 20
    fun monthShortAndDate(): DateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

    // Locale independent time only - ie, 3:30 PM or 15:30
    fun timeOnlyInternational(): DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    // Locale independent date only - ie, MM/dd/yy or dd/MM/yy
    fun dateOnlyInternational(): DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

}
