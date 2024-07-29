package com.abdelrahman.raafat.climateClue.model

import androidx.room.*
import com.abdelrahman.raafat.climateClue.database.DataConverter
import org.jetbrains.annotations.Nullable

@Entity(tableName = "weather")
data class WeatherResponse
    (
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Double,

    @Embedded(prefix = "current_")
    var current: Current,

    @TypeConverters(DataConverter::class)
    var minutely: List<Minutely>?,

    @TypeConverters(DataConverter::class)
    var hourly: List<Hourly>,

    @TypeConverters(DataConverter::class)
    var daily: List<Daily>,

    @Nullable
    @TypeConverters(DataConverter::class)
    var alerts: List<Alerts>?


)

@TypeConverters(DataConverter::class)
data class Current(
    var dt: Long,
    var sunrise: Long,
    var sunset: Long,
    var temp: Double,
    var feels_like: Double,
    var pressure: Long,
    var humidity: Long,
    var dew_point: Double,
    var uvi: Double,
    var clouds: Long,
    var visibility: Long,
    var wind_speed: Double,
    var wind_deg: Long,
    var wind_gust: Double,
    var weather: ArrayList<Weather>
)

@TypeConverters(DataConverter::class)
data class Weather(
    var id: Long,
    var main: String,
    var description: String,
    var icon: String,
)

@TypeConverters(DataConverter::class)
data class Minutely(
    var dt: Long,
    var precipitation: Long
)

@TypeConverters(DataConverter::class)
data class Hourly(
    var dt: Long,
    var temp: Double,
    var feels_like: Double,
    var pressure: Long,
    var humidity: Long,
    var dew_point: Double,
    var uvi: Double,
    var clouds: Long,
    var visibility: Long,
    var wind_speed: Double,
    var wind_deg: Long,
    var wind_gust: Double,
    var weather: ArrayList<Weather>,
    var pop: Double
)

@TypeConverters(DataConverter::class)
data class Daily(
    var dt: Long,
    var sunrise: Long,
    var sunset: Long,
    var moonrise: Long,
    var moonset: Long,
    var moon_phase: Double,
    var temp: Temperature,
    var feels_like: FeelsLike,
    var pressure: Long,
    var humidity: Int,
    var dew_point: Double,
    var wind_speed: Double,
    var wind_deg: Long,
    var wind_gust: Double,
    var weather: ArrayList<Weather>,
    var clouds: Int,
    var pop: Double,
    var uvi: Double
)

@TypeConverters(DataConverter::class)
data class Alerts(
    var sender_name: String,
    var event: String,
    var start: Long,
    var end: Long,
    var description: String,
    var tags: ArrayList<String>
)

data class FeelsLike(
    var day: Double,
    var night: Double,
    var eve: Double,
    var morn: Double,
)

data class Temperature(
    var day: Double,
    var min: Double,
    var max: Double,
    var night: Double,
    var eve: Double,
    var morn: Double,
)









