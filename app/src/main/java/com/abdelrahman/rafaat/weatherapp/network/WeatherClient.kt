package com.abdelrahman.rafaat.weatherapp.network

import android.util.Log
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse


object WeatherClient : RemoteSource {

    private val TAG = "WeatherClient"
    private var client: WeatherClient? = null

    fun getInstance(): WeatherClient {
        if (client == null) {
            client = WeatherClient
        }
        return client!!
    }

    val retrofitHelper = RetrofitHelper.getClient().create(WeatherService::class.java)
    override suspend fun getWeatherData(): WeatherResponse? {
        val response = retrofitHelper.getWeatherDataArabic("33.44", "-94.04", ConstantsValue.language)
        val weatherResponse = response.body()
        return weatherResponse
    }

    /*
    override suspend fun getWeatherDataDefault() {
        val response = retrofitHelper.getWeatherDataDefault("33.44", "-94.04")
        val weatherResponse = response.body()
        Log.i(TAG, "getWeatherDataDefault: response ----> " + response)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.lat)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.lon)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.timezone)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.timezone_offset)
        Log.i(TAG, "getWeatherDataArabic: \n\n\n\n\n")
    }*/

    override suspend fun getWeatherDataArabic(): WeatherResponse? {
        val response = retrofitHelper.getWeatherDataArabic("33.44", "-94.04", "ar")
        val weatherResponse = response.body()

        Log.i(TAG, "getWeatherDataArabic: response ----> " + response)
        Log.i(TAG, "getWeatherDataArabic: response ----> " + weatherResponse!!.hourly[0].clouds)
        Log.i(
            TAG,
            "getWeatherDataArabic: response ----> " + weatherResponse!!.hourly[0].weather[0].description
        )
        Log.i(TAG, "getWeatherDataArabic: response ----> " + weatherResponse!!.daily[0].clouds)
        Log.i(
            TAG,
            "getWeatherDataArabic: response ----> " + weatherResponse!!.daily[0].weather[0].description
        )
        Log.i(TAG, "getWeatherDataArabic: \n\n\n\n\n")
        return weatherResponse
    }

    override suspend fun getWeatherDataEnglish(): WeatherResponse? {
        val response = retrofitHelper.getWeatherDataEnglish("33.44", "-94.04", "en")
        val weatherResponse = response.body()
        Log.i(TAG, "getWeatherDataUnits: response ----> " + response)
        Log.i(TAG, "getWeatherDataUnits: response ----> " + weatherResponse!!.current.temp)
        Log.i(
            TAG,
            "getWeatherDataUnits: response ----> " + weatherResponse!!.current.weather[0].description
        )
        Log.i(TAG, "getWeatherDataUnits: response ----> " + weatherResponse!!.minutely[0].dt)
        Log.i(
            TAG,
            "getWeatherDataUnits: response ----> " + weatherResponse!!.minutely[0].precipitation
        )

        return weatherResponse
    }
}