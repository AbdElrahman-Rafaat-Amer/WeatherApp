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

    /* override suspend fun getWeatherFromNetwork(): WeatherResponse {
         Log.i(TAG, "getWeatherFromNetwork:getWeatherFromNetwork ")
         return remoteSource.getWeatherData()
     }*/

    override suspend fun getWeatherFromNetwork(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse {
        Log.i(TAG, "getWeatherFromNetwork: getWeatherFromNetwork parameterized")

        return remoteSource.getWeatherData(latitude, longitude, language)
    }

    override suspend fun getWeatherFromDataBase(): WeatherResponse {
        TODO("Not yet implemented")
    }

    override suspend fun insertToFavorite() {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavorite() {
        TODO("Not yet implemented")
    }
}