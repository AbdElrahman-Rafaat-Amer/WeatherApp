package com.abdelrahman.rafaat.weatherapp.model


import android.location.Address
import com.abdelrahman.rafaat.weatherapp.R
import java.text.DecimalFormat


class ConstantsValue {
    companion object {
        var address: Address? = null
        var language: String = ""
        var longitude: String = ""
        var latitude: String = ""
        var tempUnit = "k"
        var windSpeedUnit = "S"
        var locationMethod = "G"
        var notificationMethod = "E"
    }

}