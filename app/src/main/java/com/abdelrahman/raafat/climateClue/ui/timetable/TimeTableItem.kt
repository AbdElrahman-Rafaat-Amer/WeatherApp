package com.abdelrahman.raafat.climateClue.ui.timetable

import com.abdelrahman.raafat.climateClue.model.DayInfo


sealed class TimeTableItem {

    data class DayItem(
        var dayName: String,
        var dayInfo: List<DayInfoItem>
    ) : TimeTableItem()

    data class DayInfoItem(
        var dayInfo: DayInfo
    ) : TimeTableItem()
}