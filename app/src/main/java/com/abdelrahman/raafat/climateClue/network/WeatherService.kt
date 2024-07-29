package com.abdelrahman.raafat.climateClue.network

import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeatherDataDefaultResponse(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("APPID") app_id: String = "cce64fba5705becc7fbe52b46e9df003"
    ): Response<WeatherResponse>

}