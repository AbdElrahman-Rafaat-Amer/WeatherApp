package com.abdelrahman.rafaat.weatherapp.network

import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse

interface RemoteSource {

    suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse

}