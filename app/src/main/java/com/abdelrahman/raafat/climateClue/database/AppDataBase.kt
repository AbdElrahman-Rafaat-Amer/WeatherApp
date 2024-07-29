package com.abdelrahman.raafat.climateClue.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdelrahman.raafat.climateClue.model.FavoritePlaces
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import com.abdelrahman.raafat.climateClue.model.SavedAlerts
import com.abdelrahman.raafat.climateClue.model.WeatherResponse


@Database(
    entities = [WeatherResponse::class, FavoritePlaces::class, SavedAddress::class, SavedAlerts::class],
    version = 17
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