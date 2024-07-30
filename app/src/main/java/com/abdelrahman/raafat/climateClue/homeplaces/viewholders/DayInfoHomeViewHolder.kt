package com.abdelrahman.raafat.climateClue.homeplaces.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.abdelrahman.raafat.climateClue.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.homeplaces.view.HomeItem

class DayInfoHomeViewHolder(itemView: View) : BaseViewHolder<HomeItem.DayInfoItem>(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val titleTextView: TextView = itemView.findViewById(R.id.title_textView)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.description_textView)

    override fun bind(position: Int, item: HomeItem.DayInfoItem) {
        imageView.setImageResource(item.icon)
        titleTextView.text = item.title
        descriptionTextView.text = item.description
    }

}