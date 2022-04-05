package com.abdelrahman.rafaat.weatherapp.timetable.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Daily
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DayAdapter(context: Context) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    private val TAG = "DayAdapter"
    private var context = context
    private var days: List<Daily> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_time_table, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        var day = days[position]
        holder.dayInformationTextView.text = getNameOfDay(day.dt)
        holder.sunRiseTimeTextView.text = getTimeInHour(day.sunrise)
        holder.sunSetTimeTextView.text = getTimeInHour(day.sunset)
        holder.moonPhaseTextView.text = getMoonPhase(day)
        holder.temperatureTextView.text = getTemperature(day)
        holder.weatherDescriptionTextView.text = day.weather[0].description
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + day.weather[0].icon + "@2x.png")
            .apply(RequestOptions.circleCropTransform())
            .into(holder.weatherImageView)

        holder.probabilityPrecipitationTextView.text =
            DecimalFormat("#").format((day.pop * 100)) + " %"
        holder.cloudTextView.text = DecimalFormat("#").format(day.clouds) + " %"
        holder.pressureTextView.text =
            DecimalFormat("#").format(day.pressure) + " " + context.getString(R.string.pressure_unit)
        holder.ultravioletTextView.text = DecimalFormat("#.##").format(day.uvi)
        holder.humidityTextView.text = DecimalFormat("#").format(day.humidity) + " %"
        holder.windSpeedTextView.text = getWindSpeed(day.wind_speed)
        holder.windDegreeTextView.text = DecimalFormat("##").format(day.wind_deg)
    }


    override fun getItemCount(): Int {
        return days.size
    }

    fun setList(days: List<Daily>) {
        Log.i(TAG, "setList: days" + days.size)
        Log.i(TAG, "setList: this.days " + this.days.size)
        this.days = days.subList(1, 8)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayInformationTextView: TextView =
            itemView.findViewById(R.id.information_of_currentDay_textView)
        val sunRiseTimeTextView: TextView = itemView.findViewById(R.id.sun_rise_time_textView_day)
        val sunSetTimeTextView: TextView = itemView.findViewById(R.id.sun_set_time_textView_day)
        val temperatureTextView: TextView = itemView.findViewById(R.id.temperature_textView_day)
        val weatherDescriptionTextView: TextView =
            itemView.findViewById(R.id.weather_description_textView_day)
        val weatherImageView: ImageView = itemView.findViewById(R.id.weather_imageView_day)
        val probabilityPrecipitationTextView: TextView =
            itemView.findViewById(R.id.probability_precipitation_textView_day)
        val cloudTextView: TextView = itemView.findViewById(R.id.cloud_textView_day)
        val pressureTextView: TextView = itemView.findViewById(R.id.pressure_textView_day)
        val moonPhaseTextView: TextView = itemView.findViewById(R.id.moon_phase_textView_day)
        val ultravioletTextView: TextView = itemView.findViewById(R.id.ultraviolet_textView_day)
        val humidityTextView: TextView = itemView.findViewById(R.id.humidity_textView_day)
        val windSpeedTextView: TextView = itemView.findViewById(R.id.wind_speed_textView_day)
        val windDegreeTextView: TextView = itemView.findViewById(R.id.wind_degree_textView_day)
    }

    private fun getNameOfDay(milliSeconds: Long): String {
        val time = milliSeconds * 1000.toLong()
        //val format = SimpleDateFormat("yyyy.MM.dd")
      //  Log.i(TAG, "getNameOfDay: " + format.format(time))
        //SimpleDateFormat("EEEE yyyy.MM.dd").format(milliSeconds * 1000)

        val dateFormat = SimpleDateFormat("EEEE d MMM yyyy", Locale(ConstantsValue.language))

        return  dateFormat.format(time)
    }

    private fun getTimeInHour(milliSeconds: Long): String {
        val time = milliSeconds * 1000.toLong()
        val format = SimpleDateFormat("h a")
        format.timeZone = TimeZone.getTimeZone("Africa/Cairo")
        return format.format(Date(time))
    }

    private fun getTemperature(day: Daily, dy: Daily): String {
        var temperature: String
        val minTemperature = DecimalFormat("#").format(day.temp.min - 273.15)
        val maxTemperature = DecimalFormat("#").format(day.temp.max - 273.15)
        temperature = "$minTemperature / $maxTemperature"
        temperature += context.getString(R.string.temperature_celsius_unit)
        return temperature
    }

    private fun getTemperature(day: Daily): String {
        var temperature = ""
        when (ConstantsValue.tempUnit) {
            "C" -> {
                temperature = DecimalFormat("#").format(day.temp.min - 273.15)
                temperature += " / "
                temperature += DecimalFormat("#").format(day.temp.max - 273.15)
                temperature += " " + context.getString(R.string.temperature_celsius_unit)
            }
            "F" -> {
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
        var phase = day.moon_phase
        when (phase) {
            in 0.0..0.24 -> moonPhase = context.getString(R.string.new_moon)
            in 0.25..0.49 -> moonPhase = context.getString(R.string.first_quarter)
            in 0.5..0.74 -> moonPhase = context.getString(R.string.full_moon)
            in 0.75..0.9 -> moonPhase = context.getString(R.string.last_quarter)
            1.0 -> moonPhase = context.getString(R.string.new_moon)
        }
        return moonPhase
    }

    private fun getWindSpeed(windSpeed : Double): String {
        var windSpeedFormat = ""
        windSpeedFormat = when (ConstantsValue.windSpeedUnit) {
            "H" -> DecimalFormat("#.##").format(windSpeed * 3.6) + " " + context.getString(
                R.string.wind_speed_unit_KH
            )
            else -> DecimalFormat("#.##").format(windSpeed) + " " + context.getString(
                R.string.wind_speed_unit_MS
            )
        }
        return windSpeedFormat
    }
}