package com.abdelrahman.raafat.climateClue.ui.home.view

import com.abdelrahman.raafat.climateClue.model.DayInfo
import com.abdelrahman.raafat.climateClue.model.Hourly

sealed class HomeItem {

    data class TitleItem(
        var title: String,
        var isBold: Boolean = false,
        var isTextLarge: Boolean = false,
        var isTextSmall: Boolean = false
    ) : HomeItem()

    data class StatusItem(
        var status: String,
        var temperature: String,
        var temperatureUnit: String,
        var iconURL: String,
        var iconPlaceHolder: Int
    ) : HomeItem()

    data class HourlyItem(
        var hourlyList: List<Hourly>
    ) : HomeItem()

    data class DailyItem(
        var dayName: String,
        var dayStatus: String,
        var dayTemperature: String,
        var iconURL: String,
        var iconPlaceHolder: Int
    ) : HomeItem()

    data class DayInfoItem(
        var dayInfo: DayInfo
    ) : HomeItem()
}