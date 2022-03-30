package com.abdelrahman.rafaat.weatherapp.database

import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse


interface LocaleSource {

    fun getWeatherFromDataBase(): WeatherResponse
    fun insertToFavorite()
    fun removeFromFavorite()
}