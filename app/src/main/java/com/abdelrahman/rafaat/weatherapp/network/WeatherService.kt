package com.abdelrahman.rafaat.weatherapp.network

import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeatherDataDefault(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("APPID") app_id: String = "cce64fba5705becc7fbe52b46e9df003"
    ): Response<WeatherResponse>

    @GET("onecall")
    suspend fun getWeatherDataArabic(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("lang") lang: String = "ar",
        @Query("APPID") app_id: String = "cce64fba5705becc7fbe52b46e9df003"
    ): Response<WeatherResponse>

    @GET("onecall")
    suspend fun getWeatherDataUnits(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ar",
        @Query("APPID") app_id: String = "cce64fba5705becc7fbe52b46e9df003"
    ): Response<WeatherResponse>

/*    @GET("onecall")
    suspend fun getFavData(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("units") units: String = "metric",//metric: Celsius, imperial: Fahrenheit.
        @Query("lang") lang: String = "ar",  //metric: metre/sec, imperial: miles/hour
        // @Query("exclude") exclude: String="current",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<FavouriteData>
*/

}