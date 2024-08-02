package com.abdelrahman.raafat.climateClue.extension

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toAbbreviatedDayName(): String {
    return SimpleDateFormat("EE", Locale.getDefault()).format(this * 1000)
}

fun Long.toFullDateString(): String {
    val time = this * 1000.toLong()
    val dateFormat = SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault())
    return dateFormat.format(time)
}