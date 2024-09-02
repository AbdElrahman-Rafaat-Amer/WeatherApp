package com.abdelrahman.raafat.climateClue.ui.home.viewholders

import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.CellHomeItemBinding
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem

class TitleHomeViewHolder(private val binding: CellHomeItemBinding) :
    BaseViewHolder<HomeItem.TitleItem>(binding.root) {

    override fun bind(position: Int, item: HomeItem.TitleItem) {
        binding.textView.text = item.title
        when {
            item.isBold -> {
                binding.textView.setTextAppearance(R.style.ClimateClue_Theme_Text_Bold)
            }

            item.isTextLarge -> {
                binding.textView.setTextAppearance(R.style.ClimateClue_Theme_Text_Large)
            }

            item.isTextSmall -> {
                binding.textView.setTextAppearance(R.style.ClimateClue_Theme_Text_Small)
            }

            else -> {
                binding.textView.setTextAppearance(R.style.ClimateClue_Theme_Text)
            }
        }
    }

}