package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import com.abdelrahman.rafaat.weatherapp.model.Daily
import com.abdelrahman.rafaat.weatherapp.model.Hourly

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
        var dailyItem: Daily,
        var dayName: String,
        var dayStatus: String,
        var dayTemperature: String,
        var iconURL: String,
        var iconPlaceHolder: Int
    ) : HomeItem()

    data class DayInfoItem(
        var icon: Int,
        var title: String,
        var description: String
    ) : HomeItem()
}