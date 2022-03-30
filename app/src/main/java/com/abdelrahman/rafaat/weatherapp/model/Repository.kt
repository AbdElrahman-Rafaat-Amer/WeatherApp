package com.abdelrahman.rafaat.weatherapp.model

class Repository : RepositoryInterface {


    override suspend fun getWeatherFromNetwork(): WeatherResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherFromDataBase(): WeatherResponse {
        TODO("Not yet implemented")
    }

    override suspend fun insertToFavorite() {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavorite() {
        TODO("Not yet implemented")
    }
}