package com.abdelrahman.raafat.climateClue.timetable

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.CellTimeTableBinding
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.model.Daily
import com.abdelrahman.raafat.climateClue.ui.base.BaseViewHolder
import com.abdelrahman.raafat.climateClue.ui.itemDecorators.SpacingItemDecoration
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.ArrayList


class TimeTableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var items: List<TimeTableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = CellTimeTableBinding.inflate(layoutInflater, parent, false)
        return TimeTableViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as? BaseViewHolder<Any>
        viewHolder?.bind(position, items[position])
//        holder.dayInformationTextView.text = getNameOfDay(day.dt)
//        holder.sunRiseTimeTextView.text = getTimeInHour(day.sunrise)
//        holder.sunSetTimeTextView.text = getTimeInHour(day.sunset)
//        holder.moonPhaseTextView.text = getMoonPhase(day)

//        holder.temperatureTextView.text = getTemperature(day)
//        holder.weatherDescriptionTextView.text = day.weather[0].description
//        Glide.with(context)
//            .load("https://openweathermap.org/img/wn/" + day.weather[0].icon + "@2x.png")
//            .apply(RequestOptions.circleCropTransform())
//            .into(holder.weatherImageView)
//
//        holder.probabilityPrecipitationTextView.text =
//            DecimalFormat("#").format((day.pop * 100)).plus(" %")
//
//
//        holder.cloudTextView.text = DecimalFormat("#").format(day.clouds).plus(" %")
//        holder.pressureTextView.text =
//            DecimalFormat("#").format(day.pressure)
//                .plus(" ${context.getString(R.string.pressure_unit)}")
//        holder.ultravioletTextView.text = DecimalFormat("#.##").format(day.uvi)
//        holder.humidityTextView.text = DecimalFormat("#").format(day.humidity).plus(" %")
//        holder.windSpeedTextView.text = getWindSpeed(day.wind_speed)
//        holder.windDegreeTextView.text = DecimalFormat("##").format(day.wind_deg)
    }


    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(days: List<TimeTableItem>) {
        this.items = days
        notifyDataSetChanged()
    }

    inner class TimeTableViewHolder(private val binding: CellTimeTableBinding) :
        BaseViewHolder<TimeTableItem.DayItem>(binding.root) {
        private val dayAdapter = DayAdapter()
        override fun bind(position: Int, item: TimeTableItem.DayItem) {
            initUI()
            binding.dayNameTextView.text = item.dayName
            dayAdapter.setData(item.dayInfo)
        }

        private fun initUI() {
            val spanCount = 3
            binding.dayInfoRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
            binding.dayInfoRecyclerView.adapter = dayAdapter
            val verticalSpace = context.resources.getDimensionPixelSize(R.dimen.vertical_space)
            val horizontalSpace = context.resources.getDimensionPixelSize(R.dimen.horizontal_space)
            binding.dayInfoRecyclerView.addItemDecoration(
                SpacingItemDecoration(
                    verticalSpace,
                    horizontalSpace,
                    spanCount = spanCount
                )
            )
        }
    }


    private fun getTimeInHour(milliSeconds: Long): String {
        val time = milliSeconds * 1000.toLong()
        val format = SimpleDateFormat("h a", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("Africa/Cairo")
        return format.format(Date(time))
    }

    private fun getTemperature(day: Daily): String {
        var temperature: String
        when (ConstantsValue.tempUnit) {
            "celsius" -> {
                temperature = DecimalFormat("#").format(day.temp.min - 273.15)
                temperature += " / "
                temperature += DecimalFormat("#").format(day.temp.max - 273.15)
                temperature += " " + context.getString(R.string.temperature_celsius_unit)
            }

            "fahrenheit" -> {
                temperature = DecimalFormat("#").format(((day.temp.min - 273.15) * 1.8) + 32)
                temperature += " / "
                temperature = DecimalFormat("#").format(((day.temp.max - 273.15) * 1.8) + 32)
                temperature += " " + context.getString(R.string.temperature_fahrenheit_unit)
            }

            else -> {
                temperature = DecimalFormat("#").format(day.temp.min)
                temperature += " / "
                temperature += DecimalFormat("#").format(day.temp.max)
                temperature += " " + context.getString(R.string.temperature_kelvin_unit)
            }
        }
        return temperature
    }

    private fun getMoonPhase(day: Daily): String {
        var moonPhase = ""
        when (day.moon_phase) {
            in 0.0..0.24 -> moonPhase = context.getString(R.string.new_moon)
            in 0.25..0.49 -> moonPhase = context.getString(R.string.first_quarter)
            in 0.5..0.74 -> moonPhase = context.getString(R.string.full_moon)
            in 0.75..0.9 -> moonPhase = context.getString(R.string.last_quarter)
            1.0 -> moonPhase = context.getString(R.string.new_moon)
        }
        return moonPhase
    }

    private fun getWindSpeed(windSpeed: Double): String {
        val windSpeedFormat: String = when (ConstantsValue.windSpeedUnit) {
            "M/H" -> DecimalFormat("#.##").format(windSpeed * 3.6) + " " + context.getString(
                R.string.wind_speed_unit_MH
            )

            else -> DecimalFormat("#.##").format(windSpeed) + " " + context.getString(
                R.string.wind_speed_unit_MS
            )
        }
        return windSpeedFormat
    }
}