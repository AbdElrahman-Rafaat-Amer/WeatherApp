package com.abdelrahman.rafaat.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable


@Entity(tableName = "address")
data class SavedAddress(

    @ColumnInfo(name = "language")
    var language: String,

    @ColumnInfo(name = "locality")
    var locality: String,

    @ColumnInfo(name = "subAdminArea")
    var subAdminArea: String,

    @ColumnInfo(name = "adminArea")
    var adminArea: String,

    @PrimaryKey
    @ColumnInfo(name = "countryName")
    var countryName: String

)