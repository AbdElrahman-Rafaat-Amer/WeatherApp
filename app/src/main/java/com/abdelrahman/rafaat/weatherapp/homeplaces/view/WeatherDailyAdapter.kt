package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Daily
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList

private const val TAG = "WeatherDailyAdapter"

class WeatherDailyAdapter :
    RecyclerView.Adapter<WeatherDailyAdapter.ViewHolder?>() {
    private lateinit var context: Context

    private var days: List<Daily> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_current_daily, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherDailyAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        val day = days[position]
        holder.dateOfDay.text = getNameOfDay(day.dt)
        holder.statusOfDay.text = day.weather[0].description
        holder.temperature.text = getTemperature(day.temp.day)
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + day.weather[0].icon + "@2x.png")
            .apply(RequestOptions.circleCropTransform())
            .into(holder.statusImage)
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + days.size)
        return days.size
    }

    fun setList(days: List<Daily>) {
        this.days = days.subList(1, 8)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateOfDay: TextView = itemView.findViewById(R.id.name_of_day_textView)
        var statusOfDay: TextView = itemView.findViewById(R.id.status_of_day_textView)
        var temperature: TextView = itemView.findViewById(R.id.temperature_of_day_textView)
        var statusImage: ImageView = itemView.findViewById(R.id.status_of_day_imageView)
        var currentDayConstraintLayout: ConstraintLayout =
            itemView.findViewById(R.id.current_day_constraintLayout)
    }

    private fun getNameOfDay(milliSeconds: Long): String {
        val time = milliSeconds * 1000.toLong()
        val format = SimpleDateFormat("yyyy.MM.dd", Locale(ConstantsValue.language))
        Log.i(TAG, "getNameOfDay: " + format.format(time))
        return SimpleDateFormat("EE").format(milliSeconds * 1000)
    }

    private fun getTemperature(day: Daily): String {

        var temperature: String
        val minTemperature = DecimalFormat("#").format(day.temp.day - 273.15)
        val maxTemperature = DecimalFormat("#").format(day.temp.max - 273.15)
        temperature = "$minTemperature / $maxTemperature"
        temperature += context.getString(R.string.temperature_celsius_unit)
        return temperature
    }


    private fun getTemperature(temp: Double): String {
        var temperature = ""
        when (ConstantsValue.tempUnit) {
            "C" -> {
                temperature = DecimalFormat("#").format(temp - 273.15)
                temperature += " " + context.getString(R.string.temperature_celsius_unit)
            }
            "F" -> {
                temperature = DecimalFormat("#").format(((temp - 273.15) * 1.8) + 32)
                temperature += " " + context.getString(R.string.temperature_fahrenheit_unit)
            }
            else -> {
                temperature = DecimalFormat("#").format(temp)
                temperature += " " + context.getString(R.string.temperature_kelvin_unit)
            }
        }
        return temperature
    }

}