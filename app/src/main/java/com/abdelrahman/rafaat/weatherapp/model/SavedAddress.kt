package com.abdelrahman.rafaat.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "address")
data class SavedAddress(

    @PrimaryKey
    @ColumnInfo(name = "language")
    var language: String,

    @ColumnInfo(name = "subAdminArea")
    var subAdminArea: String,

    @ColumnInfo(name = "adminArea")
    var adminArea: String,

    @ColumnInfo(name = "countryName")
    var countryName: String

)