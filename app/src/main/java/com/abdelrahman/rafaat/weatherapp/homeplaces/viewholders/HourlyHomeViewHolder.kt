package com.abdelrahman.rafaat.weatherapp.homeplaces.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.base.BaseViewHolder
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeItem
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.WeatherHourlyAdapter
import com.abdelrahman.rafaat.weatherapp.utils.SpacingItemDecoration

class HourlyHomeViewHolder(itemView: View) : BaseViewHolder<HomeItem.HourlyItem>(itemView) {
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
    private val weatherHourlyAdapter = WeatherHourlyAdapter()

    override fun bind(position: Int, item: HomeItem.HourlyItem) {
        val hourlyManager = LinearLayoutManager(itemView.context)
        hourlyManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = hourlyManager
        recyclerView.adapter = weatherHourlyAdapter
        weatherHourlyAdapter.setList(item.hourlyList)
        val horizontalSpace = itemView.resources.getDimensionPixelSize(R.dimen.horizontal_space_small)
        recyclerView.addItemDecoration(SpacingItemDecoration(0, horizontalSpace, false))
    }

}