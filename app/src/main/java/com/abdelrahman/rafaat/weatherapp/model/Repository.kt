package com.abdelrahman.rafaat.weatherapp.model

import android.content.Context
import com.abdelrahman.rafaat.weatherapp.database.LocaleSource
import com.abdelrahman.rafaat.weatherapp.network.RemoteSource

class Repository : RepositoryInterface {
    var context: Context
    lateinit var remoteSource: RemoteSource
    lateinit var localSource: LocaleSource

    private constructor(context: Context, remoteSource: RemoteSource, localSource: LocaleSource) {
        this.context = context
        this.remoteSource = remoteSource
        this.localSource = localSource
    }

    companion object {
        var movieRepo: Repository? = null

        fun getInstance(
            context: Context,
            remoteSource: RemoteSource,
            localSource: LocaleSource
        ): Repository? {
            if (movieRepo == null) {
                movieRepo = Repository(context, remoteSource, localSource)
            }
            return movieRepo
        }
    }

    override suspend fun getWeatherFromNetwork(): WeatherResponse {
      return remoteSource.getWeatherData()!!
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