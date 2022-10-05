package com.abdelrahman.rafaat.weatherapp.database

import androidx.lifecycle.LiveData
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse


interface LocaleSource {

    //Current
    suspend fun getWeatherFromDataBase(): WeatherResponse?
    suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse)


    //Address
    suspend fun getStoredPlace(): SavedAddress
    suspend fun insertAddressToRoom(address: SavedAddress)

    //Alerts
    suspend fun getStoredAlerts(): List<SavedAlerts>?
    suspend fun insertAlertToRoom(alerts: SavedAlerts)
    suspend fun deleteAlertFromRoom(id: Int)
    suspend fun getAlertFromRoom(id: Int): SavedAlerts

    //Favorites
    suspend fun getFavoriteFromDataBase(): List<FavoritePlaces>
    suspend fun insertToFavorite(favoritePlaces: FavoritePlaces)
    suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces)

}