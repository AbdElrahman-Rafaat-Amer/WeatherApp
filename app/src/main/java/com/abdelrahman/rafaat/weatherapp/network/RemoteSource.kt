package com.abdelrahman.rafaat.weatherapp.network

import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse

interface RemoteSource {
    suspend fun getWeatherData(): WeatherResponse?

    suspend fun getWeatherDataArabic(): WeatherResponse?

    suspend fun getWeatherDataEnglish(): WeatherResponse?
}