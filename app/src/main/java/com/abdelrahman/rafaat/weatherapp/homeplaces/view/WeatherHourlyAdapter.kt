package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Hourly
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.Format
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherHourlyAdapter(context: Context) :
    RecyclerView.Adapter<WeatherHourlyAdapter.ViewHolder>() {

    private var context = context
    private val TAG = "WeatherHourlyAdapter"
    private var hours: List<Hourly> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHourlyAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        val hour = hours[position]
        holder.timeInHour.text = getTimeInHour(hour.dt)
        holder.temperature.text = getTemperature(hour.temp)
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + hour.weather[0].icon + "@2x.png")
            .apply(RequestOptions.circleCropTransform())
            .into(holder.statusImage)
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + hours.size)
        return hours.size
    }

    fun setList(hours: List<Hourly>) {

        this.hours = hours.subList(0, 24)
        Log.i(TAG, "setList: after")
        Log.i(TAG, "setList: hours" + hours.size)
        Log.i(TAG, "setList: this.hours " + this.hours.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeInHour: TextView = itemView.findViewById(R.id.hour_hourlyCustomRow)
        var temperature: TextView = itemView.findViewById(R.id.temperature_hourlyCustomRow)
        var statusImage: ImageView = itemView.findViewById(R.id.status_image_hourlyCustomRow)
    }


    private fun getTimeInHour(milliSeconds: Long): String {
        val time = milliSeconds * 1000.toLong()
        val format = SimpleDateFormat("h a", Locale(ConstantsValue.language))
        return format.format(Date(time))
    }

    private fun getTemperature(temp: Double): String {
        var temperature: String
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