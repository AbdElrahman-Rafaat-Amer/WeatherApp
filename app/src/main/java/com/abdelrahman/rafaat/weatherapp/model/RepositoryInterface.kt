package com.abdelrahman.rafaat.weatherapp.model

import androidx.lifecycle.LiveData


interface RepositoryInterface {

    //Network
    suspend fun getWeatherFromNetwork(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse

    //Room

    //Current
    suspend fun getWeatherFromDataBase(): WeatherResponse?
    suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse)


    //Address
    suspend fun getStoredPlace(): SavedAddress
    suspend fun insertAddressToRoom(address: SavedAddress)


    //Favorites
    suspend fun getFavoriteFromDataBase(): List<FavoritePlaces>
    suspend fun insertToFavorite(favoritePlaces: FavoritePlaces)
    suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces)


    //Alerts
    suspend fun insertAlertToRoom(savedAlerts: SavedAlerts)
    suspend fun getStoredAlerts(): List<SavedAlerts>?
    suspend fun deleteAlertFromRoom(id: Int)
    suspend fun getAlertFromRoom(id: Int): SavedAlerts


}