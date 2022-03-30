package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import com.abdelrahman.rafaat.weatherapp.model.Daily

interface OnDayClickListener {
    fun showDayDetails(dayStatus: Daily)
}