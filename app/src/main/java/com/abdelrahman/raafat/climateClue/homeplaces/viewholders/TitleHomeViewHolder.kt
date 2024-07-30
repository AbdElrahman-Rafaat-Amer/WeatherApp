package com.abdelrahman.raafat.climateClue.homeplaces.viewholders

import android.view.View
import android.widget.TextView
import com.abdelrahman.raafat.climateClue.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.homeplaces.view.HomeItem

class TitleHomeViewHolder(itemView: View) : BaseViewHolder<HomeItem.TitleItem>(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.textView)

    override fun bind(position: Int, item: HomeItem.TitleItem) {
        textView.text = item.title
        when {
            item.isBold -> {
                textView.setTextAppearance(R.style.ClimateClue_Theme_Text_Bold)
            }

            item.isTextLarge -> {
                textView.setTextAppearance(R.style.ClimateClue_Theme_Text_Large)
            }

            item.isTextSmall -> {
                textView.setTextAppearance(R.style.ClimateClue_Theme_Text_Small)
            }

            else -> {
                textView.setTextAppearance(R.style.ClimateClue_Theme_Text)
            }
        }
    }

}