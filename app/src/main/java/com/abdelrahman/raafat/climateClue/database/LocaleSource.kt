package com.abdelrahman.raafat.climateClue.database

import com.abdelrahman.raafat.climateClue.model.FavoritePlaces
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import com.abdelrahman.raafat.climateClue.model.SavedAlerts
import com.abdelrahman.raafat.climateClue.model.WeatherResponse


interface LocaleSource {

    //Current
    suspend fun getWeatherFromDataBase(): WeatherResponse?
    suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse)


    //Address
    suspend fun getStoredPlace(): SavedAddress?
    suspend fun insertAddressToRoom(address: SavedAddress)

    //Alerts
    suspend fun getStoredAlerts(): List<SavedAlerts>
    suspend fun insertAlertToRoom(alerts: SavedAlerts) : Long
    suspend fun deleteAlertFromRoom(id: Int) : Int
    suspend fun getAlertFromRoom(id: Int): SavedAlerts

    //Favorites
    suspend fun getFavoriteFromDataBase(): List<FavoritePlaces>
    suspend fun insertToFavorite(favoritePlaces: FavoritePlaces)
    suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces) : Int

}