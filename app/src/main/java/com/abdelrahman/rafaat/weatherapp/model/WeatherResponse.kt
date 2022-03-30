package com.abdelrahman.rafaat.weatherapp.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class WeatherResponse
    (
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Double,
    var current: Current,
    var minutely: List<Minutely>,
    var hourly: List<Hourly>,
    var daily: List<Daily>

)

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

data class Weather(
    var id: Long,
    var main: String,
    var description: String,
    var icon: String,
)

data class Minutely(
    var dt: Double,
    var precipitation: Double
)

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










