package com.abdelrahman.raafat.climateClue.homeplaces.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.model.Hourly
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "WeatherHourlyAdapter"

class WeatherHourlyAdapter :
    RecyclerView.Adapter<WeatherHourlyAdapter.ViewHolder>() {

    private lateinit var context: Context

    private var hours: List<Hourly> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHourlyAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: $position")
        val hour = hours[position]
        holder.timeInHour.text = getTimeInHour(hour.dt)
        holder.temperature.text = getTemperature(hour.temp)
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + hour.weather[0].icon + "@2x.png")
            .apply(RequestOptions.circleCropTransform())
            .into(holder.statusImage)
    }

    override fun getItemCount(): Int {
        // Log.i(TAG, "getItemCount: " + hours.size)
        return hours.size
    }

    fun setList(hours: List<Hourly>) {
        this.hours = hours.subList(0, 24)
        notifyDataSetChanged()
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
            "celsius" -> {
                temperature = DecimalFormat("#").format(temp - 273.15)
                temperature += " " + context.getString(R.string.temperature_celsius_unit)
            }
            "fahrenheit" -> {
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