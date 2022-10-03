package com.abdelrahman.rafaat.weatherapp.database

import android.content.Context
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.model.*
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue

private const val TAG = "ConcreteLocaleSource"

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
        Log.i(TAG, "getWeatherFromDataBase: ")
        val response = weatherDao?.getStoredWeather()!!
        Log.i(TAG, "getWeatherFromDataBase: ------------------> $response")
        return weatherDao?.getStoredWeather()!!
    }

    override suspend fun getStoredPlace(): SavedAddress {
        return weatherDao?.getStoredPlace(ConstantsValue.language)!!
    }

    override suspend fun insertAddressToRoom(address: SavedAddress) {
        Log.i(TAG, "insertAddressToRoom: address------> $address")
        val response = weatherDao?.insertAddressToRoom(address)
        Log.i(TAG, "insertAddressToRoom: response $response")
    }

    override suspend fun getFavoriteFromDataBase(): List<FavoritePlaces> {
        return weatherDao!!.getStoredFavoritePlaces()
    }

    override suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse) {
        Log.i(TAG, "insertCurrentDataToRoom:\n\n\n\n\n ")
        Log.i(TAG, "insertCurrentDataToRoom: weatherResponse")
        val response = weatherDao?.insertCurrentToRoom(weatherResponse)
        Log.i(TAG, "insertCurrentDataToRoom: response from room -----> $response")
    }

    override suspend fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        Log.i(TAG, "insertToFavorite: favoritePlaces---------> $favoritePlaces")
        weatherDao!!.insertToFavorite(favoritePlaces)
    }

    override suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces) {
        Log.i(TAG, "removeFromFavorite: favoritePlaces --------> $favoritePlaces")
        weatherDao!!.deleteFromRoom(favoritePlaces)
    }


    //-----------------------------------------------------------------------------

    //Alerts
    override suspend fun getStoredAlerts(): List<SavedAlerts>? {
        val response = weatherDao?.getStoredAlerts()
        Log.i(TAG, "getStoredAlerts: response --------> $response")
        return response
    }

    override suspend fun insertAlertToRoom(alerts: SavedAlerts) {
        val response = weatherDao?.insertAlertToRoom(alerts)
        Log.i(TAG, "insertAlertToRoom: response --------> $response")
    }

    override suspend fun deleteAlertFromRoom(id: Int) {
        val response = weatherDao?.deleteAlertFromRoom(id)
        Log.i(TAG, "deleteAlertFromRoom: response--> $response")
    }

    override suspend fun getAlertFromRoom(id: Int): SavedAlerts {
        val response = weatherDao?.getAlertFromRoom(id)
        Log.i(TAG, "getAlertFromRoom: response-----> $response")
        return response!!
    }

}