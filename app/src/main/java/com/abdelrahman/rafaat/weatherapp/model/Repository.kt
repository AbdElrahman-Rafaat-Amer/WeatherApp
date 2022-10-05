package com.abdelrahman.rafaat.weatherapp.model

import android.content.Context
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.database.LocaleSource
import com.abdelrahman.rafaat.weatherapp.network.RemoteSource

class Repository private constructor(
    var context: Context,
    private var remoteSource: RemoteSource,
    var localSource: LocaleSource
) : RepositoryInterface {

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
        val response = remoteSource.getWeatherData(latitude, longitude, language)
        localSource.insertCurrentDataToRoom(response)
        return response
    }


    //------------------------------------------------------- Room--------------------------------------------------

    //Current
    override suspend fun getWeatherFromDataBase(): WeatherResponse? {
        return localSource.getWeatherFromDataBase()
    }

    override suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse) {
        localSource.insertCurrentDataToRoom(weatherResponse)
    }


    //Address
    override suspend fun getStoredPlace(): SavedAddress {
        return localSource.getStoredPlace()
    }

    override suspend fun insertAddressToRoom(address: SavedAddress) {
        localSource.insertAddressToRoom(address)
    }


    //Favorites
    override suspend fun getFavoriteFromDataBase(): List<FavoritePlaces> {
        Log.i("Favorite", "getFavoriteFromDataBase Repo before")
        val response = localSource.getFavoriteFromDataBase()
        Log.i("Favorite", "getFavoriteFromDataBase Repo after--------> ${response.size}")
        return response
    }

    override suspend fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        localSource.insertToFavorite(favoritePlaces)
    }

    override suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces): Int {
        Log.i("Favorite", "delete Repo before")
        val deleteResponse = localSource.removeFromFavorite(favoritePlaces)
        Log.i("Favorite", "removeFromFavorite:after--------------> $deleteResponse")
        return deleteResponse
    }

    //Alert
    override suspend fun insertAlertToRoom(savedAlerts: SavedAlerts) {
        localSource.insertAlertToRoom(savedAlerts)
    }

    override suspend fun getStoredAlerts(): List<SavedAlerts>? {
        return localSource.getStoredAlerts()
    }

    override suspend fun deleteAlertFromRoom(id: Int) {
        localSource.deleteAlertFromRoom(id)
    }

    override suspend fun getAlertFromRoom(id: Int): SavedAlerts {
        return localSource.getAlertFromRoom(id)
    }

}