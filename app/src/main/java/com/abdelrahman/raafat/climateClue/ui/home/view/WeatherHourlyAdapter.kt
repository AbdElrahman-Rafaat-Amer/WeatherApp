package com.abdelrahman.raafat.climateClue.ui.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.abdelrahman.raafat.climateClue.databinding.CustomRowHourlyBinding
import com.abdelrahman.raafat.climateClue.model.Hourly
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.formatTemperature
import com.abdelrahman.raafat.climateClue.utils.formatTime
import java.util.TimeZone

class WeatherHourlyAdapter :
    RecyclerView.Adapter<WeatherHourlyAdapter.HourlyViewHolder>() {

    private lateinit var context: Context

    private var hours: List<Hourly> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        context = parent.context
        val binding = CustomRowHourlyBinding.inflate(LayoutInflater.from(context))
        return HourlyViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val viewHolder = holder as? BaseViewHolder<Any>
        viewHolder?.bind(position, hours[position])
    }

    override fun getItemCount() = hours.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(hours: List<Hourly>) {
        this.hours = hours.subList(0, 24)
        notifyDataSetChanged()
    }

    inner class HourlyViewHolder(val binding: CustomRowHourlyBinding) :
        BaseViewHolder<Hourly>(binding.root) {
        override fun bind(position: Int, item: Hourly) {
            binding.hourHourlyCustomRow.text =
                formatTime(item.dt, TimeZone.getDefault().toString(), false)
            val temp = formatTemperature(item.temp, context)
            binding.temperatureHourlyCustomRow.text = temp.first.plus(temp.second)
            val url = ConstantsValue.IMAGE_URL + item.weather[0].icon + "@2x.png"
            binding.statusImageHourlyCustomRow.load(url) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

}