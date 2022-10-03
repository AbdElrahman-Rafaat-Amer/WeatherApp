package com.abdelrahman.rafaat.weatherapp.utils

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import java.io.IOException
import java.util.*

fun connectInternet(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    } else {
        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}

fun getAddress(latitude: Double, longitude: Double, context: Context): SavedAddress {
    var address: SavedAddress? = null
    val result = StringBuilder()
    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses.size > 0) {
            result.append(addresses[0].locality).append("\n")
            result.append(addresses[0].countryName)
        }
        address = SavedAddress(
            ConstantsValue.language,
            addresses[0].subAdminArea,
            addresses[0].adminArea, addresses[0].countryName
        )

    } catch (e: IOException) {
        Log.i("TAG", "getAddress: -------------------- ${e.message}")
    }
    return address!!
}


