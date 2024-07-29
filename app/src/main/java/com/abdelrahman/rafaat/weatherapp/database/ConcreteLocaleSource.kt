package com.abdelrahman.rafaat.weatherapp.database

import android.content.Context
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

    override suspend fun getWeatherFromDataBase(): WeatherResponse? {
        return weatherDao?.getStoredWeather()
    }

    override suspend fun getStoredPlace(): SavedAddress? {
        return weatherDao?.getStoredPlace(ConstantsValue.language)
    }

    override suspend fun insertAddressToRoom(address: SavedAddress) {
        weatherDao?.insertAddressToRoom(address)
    }

    override suspend fun getFavoriteFromDataBase(): List<FavoritePlaces> {
        return weatherDao!!.getStoredFavoritePlaces()
    }

    override suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse) {
        weatherDao?.insertCurrentToRoom(weatherResponse)
    }

    override suspend fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        weatherDao!!.insertToFavorite(favoritePlaces)
    }

    override suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces): Int {
        return weatherDao!!.deleteFromRoom(favoritePlaces)
    }

    //-----------------------------------------------------------------------------

    //Alerts
    override suspend fun getStoredAlerts(): List<SavedAlerts> {
        return weatherDao!!.getStoredAlerts()
    }

    override suspend fun insertAlertToRoom(alerts: SavedAlerts): Long {
        return weatherDao!!.insertAlertToRoom(alerts)
    }

    override suspend fun deleteAlertFromRoom(id: Int): Int {
        return weatherDao!!.deleteAlertFromRoom(id)
    }

    override suspend fun getAlertFromRoom(id: Int): SavedAlerts {
        val response = weatherDao?.getAlertFromRoom(id)
        return response!!
    }

}