package com.abdelrahman.rafaat.weatherapp.network

interface RemoteSource {
    suspend fun getWeatherDataDefault()

    suspend fun getWeatherDataArabic()

    suspend fun getWeatherDataUnits()
}