package com.abdelrahman.raafat.climateClue.extension

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Extension function to convert a Unix timestamp (in seconds) to an abbreviated
 * day name (e.g., "Mon", "Tue"), formatted as a string.
 *
 * @receiver Long - Unix timestamp in seconds
 * @return String - Abbreviated day name
 */
fun Long.toAbbreviatedDayName(): String {
    return SimpleDateFormat("EE", Locale.getDefault()).format(this * 1000)
}

/**
 * Extension function to convert a Unix timestamp (in seconds) to a full date string
 * (e.g., "Monday 1 Aug 2023"), formatted as a string.
 *
 * @receiver Long - Unix timestamp in seconds
 * @return String - Full date string in the format "DayOfWeek Day Month Year"
 */
fun Long.toFullDateString(): String {
    val time = this * 1000.toLong()
    val dateFormat = SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault())
    return dateFormat.format(time)
}