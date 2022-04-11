package com.abdelrahman.rafaat.weatherapp.model

import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.util.Log
import java.io.IOException
import java.util.*

fun getAddress(latitude: Double, longitude: Double, context: Context): SavedAddress {
    var address: SavedAddress? = null
    val result = StringBuilder()
    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        val geocoder2 = Geocoder(context, Locale.ENGLISH)
        val geocoder3 = Geocoder(context, Locale.forLanguageTag("ar"))


        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val addresses3 = geocoder3.getFromLocation(latitude, longitude, 1)
        val addresses2 = geocoder2.getFromLocation(latitude, longitude, 1)

        if (addresses.size > 0) {
            val address = addresses[0]
            result.append(address.locality).append("\n")
            result.append(address.countryName)
        }
        address = SavedAddress(
            ConstantsValue.language,
            addresses[0].subAdminArea,
            addresses[0].adminArea, addresses[0].countryName
        )

    } catch (e: IOException) {
        Log.e("TAG", e.localizedMessage)
    }
    return address!!
}

fun isInternetAvailable(context: Context): Boolean {
    var isAvailable = false
    val manager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnected || manager.getNetworkInfo(
            ConnectivityManager.TYPE_MOBILE
        )!!
            .isConnected
    ) isAvailable = true
    return isAvailable
}

