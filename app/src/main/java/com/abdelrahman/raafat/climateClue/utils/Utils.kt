package com.abdelrahman.raafat.climateClue.utils

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.extension.toCelsius
import com.abdelrahman.raafat.climateClue.extension.toFahrenheit
import com.abdelrahman.raafat.climateClue.extension.toKelvin
import com.abdelrahman.raafat.climateClue.model.Daily
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts milliseconds to a formatted time string based on the specified pattern and time zone.
 *
 * @param seconds The time in seconds.
 * @param timeZone The desired time zone ID (e.g., "GMT", "America/New_York").
 * @param includeMinutes Whether to include minutes in the output. Default is true.
 * @param use24HourFormat Whether to use 24-hour format. Default is false (uses AM/PM).
 * @return A formatted time string in the specified time zone.
 */
fun formatTime(
    seconds: Long,
    timeZone: String,
    includeMinutes: Boolean = true,
    use24HourFormat: Boolean = false
): String {
    // Convert seconds to milliSeconds
    val milliSeconds = seconds * 1000

    // Create a Date object from the given time
    val date = Date(milliSeconds)

    // Choose the appropriate format pattern
    val pattern = when {
        use24HourFormat && includeMinutes -> "HH:mm"
        use24HourFormat && !includeMinutes -> "HH"
        !use24HourFormat && includeMinutes -> "h:mm a"
        else -> "h a"
    }

    // Set up the date format with the specified locale and pattern
    val format = SimpleDateFormat(pattern, Locale(ConstantsValue.language))

    // Set the desired time zone
    format.timeZone = TimeZone.getTimeZone(timeZone)

    // Format the date and return the resulting string
    return format.format(date)
}

fun getMoonPhase(day: Daily, context: Context): String {
    var moonPhase = ""
    when (day.moon_phase) {
        in 0.0..0.24 -> moonPhase = context.getString(R.string.new_moon)
        in 0.25..0.49 -> moonPhase = context.getString(R.string.first_quarter)
        in 0.5..0.74 -> moonPhase = context.getString(R.string.full_moon)
        in 0.75..0.9 -> moonPhase = context.getString(R.string.last_quarter)
        1.0 -> moonPhase = context.getString(R.string.new_moon)
    }
    return moonPhase
}

/**
 * Formats the daily temperature range as a string with the appropriate unit.
 *
 * @param daily The daily temperature data.
 * @return A formatted string representing the temperature range.
 */
fun formatDailyTemperatureRange(daily: Daily, context: Context): String {
    val minTemp = formatTemperature(daily.temp.min, context)
    val maxTemp = formatTemperature(daily.temp.max, context)
    return "${minTemp.first} / ${maxTemp.first} ${minTemp.second}"
}

/**
 * Converts a temperature value to a formatted string with the appropriate unit.
 *
 * @param temp The temperature value in Kelvin.
 * @return A pair containing the formatted temperature string and its unit.
 */
fun formatTemperature(temp: Double, context: Context): Pair<String, String> {
    val temperature = when (ConstantsValue.tempUnit) {
        TemperatureType.Celsius.unit -> temp.toCelsius()
        TemperatureType.Fahrenheit.unit -> temp.toFahrenheit()
        else -> temp.toKelvin()
    }
    val unit = TemperatureType.getLocalizedUnit(context, ConstantsValue.tempUnit)
    return Pair(temperature, unit)
}

fun formatDate(dateInSeconds: Long): String {
    val time = dateInSeconds * 1000.toLong()
    val date = Date(time)
    val dateFormat = SimpleDateFormat("d MMM yyyy", Locale(ConstantsValue.language))
    return dateFormat.format(date)
}

fun connectInternet(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    } else {
        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}

fun getAddress(latitude: Double, longitude: Double, context: Context): SavedAddress {
    var address: SavedAddress = SavedAddress("En", "Alex", "Alex", "Egypt")
    var subAdminArea = context.getString(R.string.undefined_place)
    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        addresses?.get(0)?.subAdminArea?.let {
            subAdminArea = it
        }

        address = SavedAddress(
            ConstantsValue.language,
            subAdminArea,
            addresses?.get(0)?.adminArea ?: "", addresses?.get(0)?.countryName ?: ""
        )

    } catch (exception: IOException) {
        Log.i("Utils", "getAddress: exception-------------------- ${exception.message}")
    }
    return address
}


