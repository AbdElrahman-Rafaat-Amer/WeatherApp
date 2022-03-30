package com.abdelrahman.rafaat.weatherapp.network

import android.util.Log


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

    override suspend fun getWeatherDataDefault() {
        val response = retrofitHelper.getWeatherDataDefault("33.44", "-94.04")
        val weatherResponse = response.body()
        Log.i(TAG, "getWeatherDataDefault: response ----> " + response)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.lat)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.lon)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.timezone)
        Log.i(TAG, "getWeatherDataDefault: response ----> " + weatherResponse!!.timezone_offset)
        Log.i(TAG, "getWeatherDataArabic: \n\n\n\n\n")
    }

    override suspend fun getWeatherDataArabic() {
        val response = retrofitHelper.getWeatherDataArabic("33.44", "-94.04", "ar")
        val weatherResponse = response.body()
        Log.i(TAG, "getWeatherDataArabic: response ----> " + response)
        Log.i(TAG, "getWeatherDataArabic: response ----> " + weatherResponse!!.hourly[0].clouds)
        Log.i(TAG, "getWeatherDataArabic: response ----> " + weatherResponse!!.hourly[0].weather[0].description)
        Log.i(TAG, "getWeatherDataArabic: response ----> " + weatherResponse!!.daily[0].clouds)
        Log.i(TAG, "getWeatherDataArabic: response ----> " + weatherResponse!!.daily[0].weather[0].description)
        Log.i(TAG, "getWeatherDataArabic: \n\n\n\n\n")
    }

    override suspend fun getWeatherDataUnits() {
        val response = retrofitHelper.getWeatherDataUnits("33.44", "-94.04", "metric", "ar")
        val weatherResponse = response.body()
        Log.i(TAG, "getWeatherDataUnits: response ----> " + response)
        Log.i(TAG, "getWeatherDataUnits: response ----> " + weatherResponse!!.current.temp)
        Log.i(TAG, "getWeatherDataUnits: response ----> " + weatherResponse!!.current.weather[0].description)
        Log.i(TAG, "getWeatherDataUnits: response ----> " + weatherResponse!!.minutely[0].dt)
        Log.i(
            TAG,
            "getWeatherDataUnits: response ----> " + weatherResponse!!.minutely[0].precipitation
        )
    }
}