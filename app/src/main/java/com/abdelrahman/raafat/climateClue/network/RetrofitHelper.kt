package com.abdelrahman.raafat.climateClue.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val API_URL = "https://api.openweathermap.org/data/2.5/"

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


}