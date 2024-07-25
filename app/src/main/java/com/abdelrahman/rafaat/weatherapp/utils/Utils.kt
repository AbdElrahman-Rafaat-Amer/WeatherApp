package com.abdelrahman.rafaat.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun getTimeInHour(milliSeconds: Long, timeZone: String): String {
    val time = milliSeconds * 1000.toLong()
    val date = Date(time)
    val format = SimpleDateFormat("h:mm a", Locale(ConstantsValue.language))
    format.timeZone = TimeZone.getTimeZone(timeZone)
    return format.format(date)
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
    var address: SavedAddress? = null
    var subAdminArea: String = ""
    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        addresses?.get(0)?.subAdminArea?.let {
            subAdminArea = it ?: context.getString(R.string.undefined_place)
        }

        address = SavedAddress(
            ConstantsValue.language,
            subAdminArea,
            addresses?.get(0)?.adminArea ?: "",   addresses?.get(0)?.countryName ?: ""
        )

    } catch (exception: IOException) {
        Log.i("Utils", "getAddress: exception-------------------- ${exception.message}")
    }
    return address!!
}


