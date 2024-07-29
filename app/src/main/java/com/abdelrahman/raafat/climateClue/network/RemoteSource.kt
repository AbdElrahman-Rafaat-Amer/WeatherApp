package com.abdelrahman.raafat.climateClue.network

import com.abdelrahman.raafat.climateClue.model.WeatherResponse

interface RemoteSource {

    suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse

}