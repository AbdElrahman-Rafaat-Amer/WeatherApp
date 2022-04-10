package com.abdelrahman.rafaat.weatherapp.model

import android.content.Context
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.database.LocaleSource
import com.abdelrahman.rafaat.weatherapp.network.RemoteSource


class Repository private constructor(
    var context: Context,
    var remoteSource: RemoteSource,
    var localSource: LocaleSource
) : RepositoryInterface {

    private val TAG = "Repository"

    companion object {
        private var weatherRepo: Repository? = null
        fun getInstance(
            context: Context,
            remoteSource: RemoteSource,
            localSource: LocaleSource
        ): Repository {

            return weatherRepo ?: Repository(context, remoteSource, localSource)
        }
    }

    //Current Retrofit
    override suspend fun getWeatherFromNetwork(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse {
        Log.i(TAG, "getWeatherFromNetwork: getWeatherFromNetwork parameterized")
        var response = remoteSource.getWeatherData(latitude, longitude, language)
        localSource.insertCurrentDataToRoom(response)
        return response
    }


    //------------------------------------------------------- Room--------------------------------------------------

    //Current
    override suspend fun getWeatherFromDataBase(): WeatherResponse? {
        Log.i(TAG, "getWeatherFromDataBase: ")
        return localSource.getWeatherFromDataBase()
    }

    override suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse) {
        Log.i(TAG, "insertCurrentDataToRoom: weatherResponse")
        localSource.insertCurrentDataToRoom(weatherResponse)
    }


    //Address
    override suspend fun getStoredPlace(): SavedAddress {
        var address = localSource.getStoredPlace()
        Log.i(TAG, "getStoredPlace: $address")
        return address
    }

    override suspend fun insertAddressToRoom(address: SavedAddress) {
        Log.i(TAG, "insertAddressToRoom: address----> $address")
        localSource.insertAddressToRoom(address)
    }


    //Favorites
    override suspend fun getFavoriteFromDataBase(): List<FavoritePlaces> {
        return localSource.getFavoriteFromDataBase()
    }

    override suspend fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        Log.i(TAG, "insertToFavorite: weatherResponse")
        localSource.insertToFavorite(favoritePlaces)
    }

    override suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces) {
        Log.i(TAG, "removeFromFavorite: weatherResponse")
        localSource.removeFromFavorite(favoritePlaces)
    }

    //Alert
    override suspend fun insertAlertToRoom(savedAlerts: SavedAlerts) {
        Log.i(TAG, "insertAlertToRoom: savedAlerts----> $savedAlerts")
        localSource.insertAlertToRoom(savedAlerts)
    }

    override suspend fun getStoredAlerts(): List<SavedAlerts>? {
        Log.i(TAG, "getStoredAlerts: ")
        var response = localSource.getStoredAlerts()
        if (response != null) {
            Log.i(TAG, "getStoredAlerts: response.size---> " + response.size)
        }
        return response
    }

    override suspend fun deleteAlertFromRoom(id: Int) {
        Log.i(TAG, "deleteAlertFromRoom: alert---> $id")
        var response = localSource.deleteAlertFromRoom(id)
        Log.i(TAG, "deleteAlertFromRoom: response-----> $response")
    }

    override suspend fun getAlertFromRoom(id: Int): SavedAlerts {
        Log.i(TAG, "getAlertFromRoom: alert---> $id")
        var response = localSource.getAlertFromRoom(id)
        Log.i(TAG, "deleteAlertFromRoom: response-----> $response")
        return response
    }

}