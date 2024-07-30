package com.abdelrahman.raafat.climateClue.homeplaces.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.CircleCropTransformation
import com.abdelrahman.raafat.climateClue.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.homeplaces.view.HomeItem

class DailyHomeViewHolder(itemView: View) : BaseViewHolder<HomeItem.DailyItem>(itemView) {
    private val dayNameTextView: TextView =
        itemView.findViewById(R.id.day_name_textView)
    private val dayStatusTextView: TextView =
        itemView.findViewById(R.id.day_status_textView)
    private val dayTemperatureTextView: TextView =
        itemView.findViewById(R.id.day_temperature_textView)
    private val dayStatusImageView: ImageView =
        itemView.findViewById(R.id.day_status_imageView)

    override fun bind(position: Int, item: HomeItem.DailyItem) {
        dayNameTextView.text = item.dayName
        dayStatusTextView.text = item.dayStatus
        dayTemperatureTextView.text = item.dayTemperature
        dayStatusImageView.load(item.iconURL) {
            crossfade(true)
            placeholder(item.iconPlaceHolder)
            error(item.iconPlaceHolder)
            transformations(CircleCropTransformation())
        }
    }

}