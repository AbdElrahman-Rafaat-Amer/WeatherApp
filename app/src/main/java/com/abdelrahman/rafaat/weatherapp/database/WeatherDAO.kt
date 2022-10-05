package com.abdelrahman.rafaat.weatherapp.database

import androidx.room.*
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse

@Dao
interface WeatherDAO {

    //Stored Current Place
    @Query("SELECT * From weather")
    suspend fun getStoredWeather(): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentToRoom(weatherResponse: WeatherResponse): Long

//-------------------------------------------------------------------------------------------

    //Alerts
    @Query("SELECT * From alerts")
    suspend fun getStoredAlerts(): List<SavedAlerts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertToRoom(alerts: SavedAlerts): Long


    @Query("DELETE FROM alerts where id = :id")
    suspend fun deleteAlertFromRoom(id: Int): Int


    @Query("select * from alerts where id = :id")
    suspend fun getAlertFromRoom(id: Int): SavedAlerts

//-------------------------------------------------------------------------------------------

    //Address
    @Query("SELECT * From address WHERE language LIKE :language " + "LIMIT 1")
    suspend fun getStoredPlace(language: String): SavedAddress

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddressToRoom(address: SavedAddress): Long

//-------------------------------------------------------------------------------------------

    //Stored from Favorite
    @Query("SELECT * From favorite")
    suspend fun getStoredFavoritePlaces(): List<FavoritePlaces>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavorite(favoritePlaces: FavoritePlaces): Long

    @Delete
    suspend fun deleteFromRoom(favoritePlaces: FavoritePlaces): Int

}