package com.abdelrahman.raafat.climateClue.ui.home.viewholders

import coil.load
import coil.transform.CircleCropTransformation
import com.abdelrahman.raafat.climateClue.databinding.CellHomeDailyBinding
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder

class DailyHomeViewHolder(private val binding: CellHomeDailyBinding) :
    BaseViewHolder<HomeItem.DailyItem>(binding.root) {

    override fun bind(position: Int, item: HomeItem.DailyItem) {
        binding.dayNameTextView.text = item.dayName
        binding.dayStatusTextView.text = item.dayStatus
        binding.dayTemperatureTextView.text = item.dayTemperature
        binding.dayStatusImageView.load(item.iconURL) {
            crossfade(true)
            placeholder(item.iconPlaceHolder)
            error(item.iconPlaceHolder)
            transformations(CircleCropTransformation())
        }
    }

}