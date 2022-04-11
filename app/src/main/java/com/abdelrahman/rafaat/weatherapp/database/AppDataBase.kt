package com.abdelrahman.rafaat.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse


@Database(
    entities = [WeatherResponse::class, FavoritePlaces::class, SavedAddress::class, SavedAlerts::class],
    version = 13
)
@TypeConverters(DataConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDAO

    companion object {
        private var instance: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase {

            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "weather"
            ).fallbackToDestructiveMigration().build()

        }
    }
}