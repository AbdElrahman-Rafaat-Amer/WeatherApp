package com.abdelrahman.rafaat.weatherapp.network

import android.util.Log
import com.abdelrahman.rafaat.weatherapp.model.Alerts
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import retrofit2.Response


class WeatherClient private constructor() : RemoteSource {

    private val TAG = "WeatherClient"

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
        Log.i(TAG, "getWeatherData: parameterized")
        val response = retrofitHelper.getWeatherDataDefaultResponse(
            /*latitude,
            longitude,*/
            "45.23",
            "37.7",
            language
        )

        Log.i(TAG, "getWeatherData: weatherResponse --------> $response")
        // Log.i(TAG, "getWeatherData: response.body() ---------> " + response.body())
        // Log.i(TAG, "getWeatherData: response.isSuccessful----> " + response.isSuccessful)
        Log.i(TAG, "getWeatherData: code " + response.code())
        Log.i(TAG, "getWeatherData: response.body() ---------> " + response.body()?.alerts)
        if (response.code() == 200) {
            Log.i(TAG, "getWeatherData: success code---> " + response.code())
        } else {
            Log.i(TAG, "getWeatherData: failed code---> " + response.code())
        }
        return response.body()!!

    }
}