package com.abdelrahman.rafaat.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "favorite")
data class FavoritePlaces(
    @ColumnInfo(name = "latitude")
    @PrimaryKey
    var lat: Double,

    @ColumnInfo(name = "longitude")
    var lng: Double,

    @ColumnInfo(name = "address")
    var selectedPlaces: String,

    @ColumnInfo(name = "date")
    var selectedDate: Long
) : Serializable