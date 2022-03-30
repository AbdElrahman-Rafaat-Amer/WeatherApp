package com.abdelrahman.rafaat.weatherapp.model

import android.location.Address

class ConstantsValue {
    companion object {
        lateinit var address: Address
        lateinit var language: String
        var longitude: Double = 0.0
        var latitude: Double = 0.0
    }
}