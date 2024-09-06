package com.abdelrahman.raafat.climateClue.ui.home.viewholders

import coil.load
import coil.transform.CircleCropTransformation
import com.abdelrahman.raafat.climateClue.databinding.CellHomeStatusBinding
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder

class StatusHomeViewHolder(private val binding: CellHomeStatusBinding) :
    BaseViewHolder<HomeItem.StatusItem>(binding.root) {

    override fun bind(position: Int, item: HomeItem.StatusItem) {
        binding.currentDayStatusTextView.text = item.status
        binding.currentDayTemperatureTextView.text = item.temperature
        binding.currentDayTemperatureUnitTextView.text = item.temperatureUnit
        binding.currentDayImageView.load(item.iconURL) {
            crossfade(true)
            placeholder(item.iconPlaceHolder)
            error(item.iconPlaceHolder)
            transformations(CircleCropTransformation())
        }
    }
}