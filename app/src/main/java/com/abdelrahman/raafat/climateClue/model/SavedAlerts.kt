package com.abdelrahman.raafat.climateClue.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
class SavedAlerts(

    @ColumnInfo(name = "start_date")
    var startDate: String,

    @ColumnInfo(name = "end_date")
    var endDate: String,

    @ColumnInfo(name = "start_time")
    var startTime: String,

    @ColumnInfo(name = "end_time")
    var endTime: String,

    @ColumnInfo(name = "repetitions")
    var repetitions: Long,

    @ColumnInfo(name = "tag")
    var tag: Long,

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int? = null,
)