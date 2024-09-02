package com.abdelrahman.raafat.climateClue.utils

import android.content.Context
import com.abdelrahman.raafat.climateClue.R

enum class WindSpeedType(val unit: String) {
    MilePerHour("M/H"),
    MeterPerSecond("M/S");

    companion object {
        fun getLocalizedUnit(mContext: Context, unit: String): String {
            return when (unit) {
                MilePerHour.unit -> mContext.getString(R.string.wind_speed_unit_MH)
                else -> {
                    mContext.getString(R.string.wind_speed_unit_MS)
                }
            }
        }
    }
}