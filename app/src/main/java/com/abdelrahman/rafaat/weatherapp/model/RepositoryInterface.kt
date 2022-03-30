package com.abdelrahman.rafaat.weatherapp.model

interface RepositoryInterface {

    //Network
    suspend fun getWeatherFromNetwork() : WeatherResponse

    //Room
    suspend fun getWeatherFromDataBase() : WeatherResponse
    suspend fun insertToFavorite()
    suspend fun removeFromFavorite()
}