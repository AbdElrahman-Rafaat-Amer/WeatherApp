package com.abdelrahman.raafat.climateClue.ui.home.viewholders

import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.databinding.CellHomeDayInfoBinding
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem

class DayInfoHomeViewHolder(private val binding: CellHomeDayInfoBinding) :
    BaseViewHolder<HomeItem.DayInfoItem>(binding.root) {

    override fun bind(position: Int, item: HomeItem.DayInfoItem) {
        binding.statusDetailsWidget.setHomeDayInfo(item)
    }

}