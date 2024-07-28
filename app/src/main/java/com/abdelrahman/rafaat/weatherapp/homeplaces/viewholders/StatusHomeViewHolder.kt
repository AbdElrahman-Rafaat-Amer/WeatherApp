package com.abdelrahman.rafaat.weatherapp.homeplaces.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.abdelrahman.rafaat.weatherapp.base.BaseViewHolder
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeItem

class StatusHomeViewHolder(itemView: View) : BaseViewHolder<HomeItem.StatusItem>(itemView) {
    private val currentDayStatusTextView: TextView =
        itemView.findViewById(R.id.current_day_status_textView)
    private val currentDayTemperatureTextView: TextView =
        itemView.findViewById(R.id.current_day_temperature_textView)
    private val currentDayTemperatureUnitTextView: TextView =
        itemView.findViewById(R.id.current_day_temperatureUnit_textView)
      private val currentDayImageView: ImageView =
        itemView.findViewById(R.id.current_day_imageView)


    override fun bind(position: Int, item: HomeItem.StatusItem) {
        currentDayStatusTextView.text = item.status
        currentDayTemperatureTextView.text = item.temperature
        currentDayTemperatureUnitTextView.text = item.temperatureUnit
        currentDayImageView.load(item.iconURL){
            crossfade(true)
            placeholder(item.iconPlaceHolder)
            error(item.iconPlaceHolder)
            transformations(CircleCropTransformation())
        }
    }
}