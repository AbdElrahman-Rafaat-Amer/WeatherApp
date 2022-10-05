package com.abdelrahman.rafaat.weatherapp.network

import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse

class WeatherClient private constructor() : RemoteSource {

    companion object {
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient {
            return instance ?: WeatherClient()
        }
    }

    private val retrofitHelper = RetrofitHelper.getClient().create(WeatherService::class.java)


    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse {
        val response = retrofitHelper.getWeatherDataDefaultResponse(
            latitude,
            longitude,
            language
        )
        return response.body()!!

    }
}