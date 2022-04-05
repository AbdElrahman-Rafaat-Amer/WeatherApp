package com.abdelrahman.rafaat.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse

//@Database(entities = [WeatherResponse::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): WeatherDAO

    companion object {
        private var instance: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "weather"
                )
                    .build()
            }
            return instance!!
        }
    }
}