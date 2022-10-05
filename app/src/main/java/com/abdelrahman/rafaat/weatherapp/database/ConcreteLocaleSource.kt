package com.abdelrahman.rafaat.weatherapp.database

import android.content.Context
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.model.*
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue

class ConcreteLocaleSource(context: Context) : LocaleSource {

    private var weatherDao: WeatherDAO?

    init {
        val dataBase = AppDataBase.getInstance(context.applicationContext)
        weatherDao = dataBase.weatherDao()
    }

    companion object {
        private var sourceConcreteClass: ConcreteLocaleSource? = null
        fun getInstance(context: Context): ConcreteLocaleSource {
            if (sourceConcreteClass == null)
                sourceConcreteClass = ConcreteLocaleSource(context)
            return sourceConcreteClass as ConcreteLocaleSource
        }
    }

    override suspend fun getWeatherFromDataBase(): WeatherResponse {
        return weatherDao?.getStoredWeather()!!
    }

    override suspend fun getStoredPlace(): SavedAddress {
        return weatherDao?.getStoredPlace(ConstantsValue.language)!!
    }

    override suspend fun insertAddressToRoom(address: SavedAddress) {
        weatherDao?.insertAddressToRoom(address)
    }

    override suspend fun getFavoriteFromDataBase(): List<FavoritePlaces> {
        Log.i("Favorite", "getFavoriteFromDataBase database before")
        val response = weatherDao!!.getStoredFavoritePlaces()
        Log.i("Favorite", "getFavoriteFromDataBase database after--------> ${response.size}")
        return response
    }

    override suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse) {
        weatherDao?.insertCurrentToRoom(weatherResponse)
    }

    override suspend fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        weatherDao!!.insertToFavorite(favoritePlaces)
    }

    override suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces): Int {
        Log.i("Favorite", "delete database before")
        val deleteResponse = weatherDao!!.deleteFromRoom(favoritePlaces)
        Log.i("Favorite", "delete database after--------> $deleteResponse")
        return deleteResponse
    }


    //-----------------------------------------------------------------------------

    //Alerts
    override suspend fun getStoredAlerts(): List<SavedAlerts>? {
        return weatherDao?.getStoredAlerts()
    }

    override suspend fun insertAlertToRoom(alerts: SavedAlerts) {
        weatherDao?.insertAlertToRoom(alerts)
    }

    override suspend fun deleteAlertFromRoom(id: Int) {
        weatherDao?.deleteAlertFromRoom(id)
    }

    override suspend fun getAlertFromRoom(id: Int): SavedAlerts {
        val response = weatherDao?.getAlertFromRoom(id)
        return response!!
    }

}