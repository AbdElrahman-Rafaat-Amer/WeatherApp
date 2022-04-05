package com.abdelrahman.rafaat.weatherapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse


class ConcreteLocaleSource (context: Context): LocaleSource {

   // private var dataBase= AppDataBase.getInstance(context.applicationContext)
   // private var movieDAO: WeatherDAO? = dataBase.movieDao()
   // private var storedMovies: LiveData<List<WeatherResponse>> = movieDAO!!.getStoredMovies()


    companion object {
        private var sourceConcreteClass: ConcreteLocaleSource? = null
        fun getInstance(context: Context): ConcreteLocaleSource {
            if (sourceConcreteClass == null)
                sourceConcreteClass = ConcreteLocaleSource(context)
            return sourceConcreteClass as ConcreteLocaleSource
        }
    }

    override fun getWeatherFromDataBase(): WeatherResponse {
        TODO("Not yet implemented")
    }

    override fun insertToFavorite() {
        TODO("Not yet implemented")
    }

    override fun removeFromFavorite() {
        TODO("Not yet implemented")
    }
}