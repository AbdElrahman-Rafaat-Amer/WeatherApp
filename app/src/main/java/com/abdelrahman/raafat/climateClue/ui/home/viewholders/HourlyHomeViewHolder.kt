package com.abdelrahman.raafat.climateClue.ui.home.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.CellHomeHourlyBinding
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem
import com.abdelrahman.raafat.climateClue.ui.home.view.WeatherHourlyAdapter
import com.abdelrahman.raafat.climateClue.ui.itemDecorators.SpacingItemDecoration

class HourlyHomeViewHolder(private val binding: CellHomeHourlyBinding) :
    BaseViewHolder<HomeItem.HourlyItem>(binding.root) {
    private val weatherHourlyAdapter = WeatherHourlyAdapter()

    override fun bind(position: Int, item: HomeItem.HourlyItem) {
        val hourlyManager = LinearLayoutManager(itemView.context)
        hourlyManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = hourlyManager
        binding.recyclerView.adapter = weatherHourlyAdapter
        weatherHourlyAdapter.setList(item.hourlyList)
        val horizontalSpace =
            itemView.resources.getDimensionPixelSize(R.dimen.horizontal_space_small)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(0, horizontalSpace, false))
    }

}