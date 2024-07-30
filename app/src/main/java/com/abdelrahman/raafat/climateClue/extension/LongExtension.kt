package com.abdelrahman.raafat.climateClue.extension

import java.text.SimpleDateFormat
import java.util.Locale

 fun Long.getDayName(): String {
    return SimpleDateFormat("EE", Locale.getDefault()).format(this * 1000)
}