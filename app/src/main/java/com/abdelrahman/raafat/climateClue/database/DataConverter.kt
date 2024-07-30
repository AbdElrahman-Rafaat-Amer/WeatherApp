package com.abdelrahman.raafat.climateClue.database

import androidx.room.TypeConverter
import com.abdelrahman.raafat.climateClue.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun minutelyListToString(minutelyList: List<Minutely>?): String? {
        if (minutelyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely>>() {}.type
        return gson.toJson(minutelyList, type)
    }

    @TypeConverter
    fun minutelyStringToList(minutelyString: String?): List<Minutely>? {
        if (minutelyString == null) {
            return emptyList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely>>() {}.type
        return gson.fromJson(minutelyString, type)
    }


    @TypeConverter
    fun hourlyListToString(hourlyList: List<Hourly>?): String? {
        if (hourlyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>>() {}.type
        return gson.toJson(hourlyList, type)
    }

    @TypeConverter
    fun hourlyStringToList(hourlyString: String?): List<Hourly>? {
        if (hourlyString == null) {
            return emptyList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(hourlyString, type)
    }

    @TypeConverter
    fun dailyListToString(dailyList: List<Daily>?): String? {
        if (dailyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>>() {}.type
        return gson.toJson(dailyList, type)
    }

    @TypeConverter
    fun dailyStringToList(dailyString: String?): List<Daily>? {
        if (dailyString == null) {
            return emptyList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>>() {}.type
        return gson.fromJson(dailyString, type)
    }

    @TypeConverter
    fun weatherListToString(weatherList: ArrayList<Weather>?): String? {
        if (weatherList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Weather>>() {}.type
        return gson.toJson(weatherList, type)
    }

    @TypeConverter
    fun weatherStringToList(weatherString: String?): ArrayList<Weather>? {
        if (weatherString == null) {
            return java.util.ArrayList()
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Weather>>() {}.type
        return gson.fromJson(weatherString, type)
    }

    @TypeConverter
    fun alertListToJson(alertList: List<Alerts>?) = Gson().toJson(alertList)

    @TypeConverter
    fun jsonToAlertList(alertString: String?): List<Alerts>? {
        alertString?.let {
            return Gson().fromJson(alertString, Array<Alerts>::class.java)?.toList()
        }
        return emptyList()
    }
}