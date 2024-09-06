package com.abdelrahman.raafat.climateClue.utils

import android.content.Context
import com.abdelrahman.raafat.climateClue.R

enum class TemperatureType(val unit: String) {
    Celsius("celsius"),
    Fahrenheit("fahrenheit"),
    Kelvin("kelvin");

    companion object {
        fun getLocalizedUnit(mContext: Context, unit: String): String {
            return when (unit) {
                Fahrenheit.unit -> mContext.getString(R.string.temperature_fahrenheit_unit)
                Kelvin.unit -> mContext.getString(R.string.temperature_kelvin_unit)
                else -> {
                    mContext.getString(R.string.temperature_celsius_unit)
                }
            }
        }
    }
}