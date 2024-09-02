package com.abdelrahman.raafat.climateClue.extension

import java.text.DecimalFormat

/**
 * Extension function to convert Kelvin to Celsius, formatted as a string.
 */
fun Double.toCelsius(): String {
    return DecimalFormat("#").format(this - 273.15)
}

/**
 * Extension function to convert Kelvin to Fahrenheit, formatted as a string.
 */
fun Double.toFahrenheit(): String {
    return DecimalFormat("#").format((this - 273.15) * 1.8 + 32)
}

/**
 * Extension function to convert Kelvin to Kelvin, formatted as a string.
 */
fun Double.toKelvin(): String {
    return DecimalFormat("#").format(this)
}