package com.abdelrahman.raafat.climateClue.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.LayoutStatusDetailsBinding
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem
import com.abdelrahman.raafat.climateClue.model.DayInfo
import com.abdelrahman.raafat.climateClue.ui.timetable.TimeTableItem
import com.google.android.material.card.MaterialCardView

class StatusDetailsWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    // ViewBinding instance for this custom view
    private val binding: LayoutStatusDetailsBinding

    init {
        // Inflate the layout and attach it to the MaterialCardView
        val inflater = LayoutInflater.from(context)
        binding = LayoutStatusDetailsBinding.inflate(inflater, this)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue))
    }

    // Sets home day information and applies the small card corner radius
    fun setHomeDayInfo(item: HomeItem.DayInfoItem) {
        val smallCornerRadius =
            resources.getDimensionPixelSize(R.dimen.card_corner_radius_small).toFloat()
        bindDayInfo(item.dayInfo, smallCornerRadius)
    }

    // Sets timetable day information with no corner radius
    fun setTimeTableDayInfo(item: TimeTableItem.DayInfoItem) {
        bindDayInfo(item.dayInfo, 0F)
    }

    // This function binds the data to the UI components for day information items
    private fun bindDayInfo(item: DayInfo, cornerRadius: Float) {
        binding.imageView.setImageResource(item.iconFromDrawable)
        binding.titleTextView.text = item.title
        binding.descriptionTextView.text = item.description
        radius = cornerRadius
    }
}