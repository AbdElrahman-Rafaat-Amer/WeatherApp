package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.utils.getAddress
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.MainActivity
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.WeatherDailyAdapter
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.WeatherHourlyAdapter
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "FavoriteDetailsFragment"

class FavoriteDetailsFragment(var viewModel: FavoritePlaceViewModel) : Fragment() {

    private lateinit var mainActivity: MainActivity

    private lateinit var locationNameTextView: TextView
    private lateinit var locationDetailsNameTextView: TextView
    private lateinit var currentDateTextView: TextView
    private lateinit var currentDayStatusTextView: TextView
    private lateinit var currentDayTemperatureTextView: TextView
    private lateinit var currentDayTemperatureUnitTextView: TextView

    //Card View
    private lateinit var sunRiseTimeTextView: TextView
    private lateinit var sunSetTimeTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var windSpeedTextView: TextView
    private lateinit var windDegreeTextView: TextView
    private lateinit var cloudTextView: TextView
    private lateinit var pressureTextView: TextView
    private lateinit var visibilityTextView: TextView
    private lateinit var ultravioletTextView: TextView


    private lateinit var currentDayImageView: CircleImageView
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var weatherDailyAdapter: WeatherDailyAdapter
    private lateinit var visibilityConstrainLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var animatedImageView: ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        observeViewModel()


    }

    private fun initUI(view: View) {
        progressBar = view.findViewById(R.id.loading_progressBar)
        animatedImageView = view.findViewById(R.id.animated_imageView)
        visibilityConstrainLayout = view.findViewById(R.id.visibility_constrainLayout)
        locationNameTextView = view.findViewById(R.id.location_name_textView)
        locationDetailsNameTextView = view.findViewById(R.id.locationDetails_name_textView)
        currentDateTextView = view.findViewById(R.id.current_date_textView)
        currentDayStatusTextView = view.findViewById(R.id.current_day_status_textView)
        currentDayTemperatureTextView = view.findViewById(R.id.current_day_temperature_textView)
        currentDayTemperatureUnitTextView =
            view.findViewById(R.id.current_day_temperatureUnit_textView)
        currentDayImageView = view.findViewById(R.id.current_day_imageView)

        hourlyRecyclerView = view.findViewById(R.id.hourly_recyclerView)
        weatherHourlyAdapter = WeatherHourlyAdapter()
        val hourlyManager = LinearLayoutManager(requireContext())
        hourlyManager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyRecyclerView.layoutManager = hourlyManager
        hourlyRecyclerView.adapter = weatherHourlyAdapter


        dailyRecyclerView = view.findViewById(R.id.daily_recyclerView)
        weatherDailyAdapter = WeatherDailyAdapter()
        val dailyManager = LinearLayoutManager(requireContext())
        dailyManager.orientation = LinearLayoutManager.VERTICAL
        dailyRecyclerView.layoutManager = dailyManager
        dailyRecyclerView.adapter = weatherDailyAdapter

        //Card View
        sunRiseTimeTextView = view.findViewById(R.id.sun_rise_time_textView)
        sunSetTimeTextView = view.findViewById(R.id.sun_set_time_textView)
        humidityTextView = view.findViewById(R.id.humidity_textView)
        windSpeedTextView = view.findViewById(R.id.wind_speed_textView)
        windDegreeTextView = view.findViewById(R.id.wind_degree_textView)
        cloudTextView = view.findViewById(R.id.cloud_textView)
        pressureTextView = view.findViewById(R.id.pressure_textView)
        visibilityTextView = view.findViewById(R.id.visibility_textView)
        ultravioletTextView = view.findViewById(R.id.ultraviolet_textView)

        animatedImageView.animate().rotation(360f).setDuration(2000).start()
    }

    private fun observeViewModel() {
        viewModel.selectedFavoritePlaces.observe(viewLifecycleOwner) {
            Log.i(TAG, "onCreateView: observe $it")
            when (it) {
                is WeatherResponse -> {
                    assignDataToView(it)
                    getAddress(it)
                }
                else -> Toast.makeText(context, "Return is null $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun assignDataToView(weatherResponse: WeatherResponse) {
        progressBar.visibility = GONE
        animatedImageView.visibility = GONE
        visibilityConstrainLayout.visibility = VISIBLE

        currentDateTextView.text = formatDate(weatherResponse.current.dt)
        currentDayStatusTextView.text = weatherResponse.current.weather[0].description
        currentDayTemperatureTextView.text = getTemperature(weatherResponse.current.temp)

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/" + weatherResponse.current.weather[0].icon + "@2x.png")
            .into(currentDayImageView)
        weatherDailyAdapter.setList(weatherResponse.daily)
        weatherHourlyAdapter.setList(weatherResponse.hourly)


        sunRiseTimeTextView.text = getTimeInHour(
            weatherResponse.current.sunrise,
            weatherResponse.timezone
        )
        sunSetTimeTextView.text = getTimeInHour(
            weatherResponse.current.sunset,
            weatherResponse.timezone
        )
        humidityTextView.text =
            DecimalFormat("#").format(weatherResponse.current.humidity).plus(" %")
        windSpeedTextView.text = getWindSpeed(weatherResponse)
        windDegreeTextView.text = DecimalFormat("##").format(weatherResponse.current.wind_deg)
        cloudTextView.text = DecimalFormat("#").format(weatherResponse.current.clouds).plus(" %")
        pressureTextView.text =
            DecimalFormat("#").format(weatherResponse.current.pressure)
                .plus(" ${getString(R.string.pressure_unit)}")
        visibilityTextView.text =
            DecimalFormat("#").format(weatherResponse.current.visibility)
                .plus(" ${getString(R.string.meter)}")
        ultravioletTextView.text = DecimalFormat("#.##").format(weatherResponse.current.uvi)

    }

    private fun getWindSpeed(weatherResponse: WeatherResponse): String {
        val windSpeed: String = when (ConstantsValue.windSpeedUnit) {
            "H" -> DecimalFormat("#.##").format(weatherResponse.current.wind_speed * 3.6) + " " + getString(
                R.string.wind_speed_unit_KH
            )
            else -> DecimalFormat("#.##").format(weatherResponse.current.wind_speed) + " " + getString(
                R.string.wind_speed_unit_MS
            )
        }
        return windSpeed
    }

    private fun getTemperature(temp: Double): String {
        val temperature: String
        when (ConstantsValue.tempUnit) {
            "C" -> {
                temperature = DecimalFormat("#").format(temp - 273.15)
                currentDayTemperatureUnitTextView.text =
                    resources.getString(R.string.temperature_celsius_unit)
            }
            "F" -> {
                temperature = DecimalFormat("#").format(((temp - 273.15) * 1.8) + 32)
                currentDayTemperatureUnitTextView.text =
                    resources.getString(R.string.temperature_fahrenheit_unit)
            }
            else -> {
                temperature = DecimalFormat("#").format(temp)
                currentDayTemperatureUnitTextView.text =
                    resources.getString(R.string.temperature_kelvin_unit)
            }
        }
        return temperature
    }

    private fun formatDate(dateInSeconds: Long): String {
        val time = dateInSeconds * 1000.toLong()
        val date = Date(time)
        Log.i(TAG, "getTimeInHour: date $date")

        val dateFormat = SimpleDateFormat("d MMM yyyy", Locale(ConstantsValue.language))
        Log.i(TAG, "formatDate : " + dateFormat.format(date))
        return dateFormat.format(date)
    }

    private fun getTimeInHour(milliSeconds: Long, timeZone: String): String {
        val time = milliSeconds * 1000.toLong()
        val date = Date(time)
        Log.i(TAG, "getTimeInHour: date $date")
        val format = SimpleDateFormat("h:mm a", Locale(ConstantsValue.language))
        format.timeZone = TimeZone.getTimeZone(timeZone)
        Log.i(TAG, format.format(date))
        return format.format(date)
    }

    private fun getAddress(weatherResponse: WeatherResponse) {
        val address = getAddress(
            weatherResponse.lat,
            weatherResponse.lon,
            requireContext()
        )
        assignAddressToView(
            address
        )
    }

    private fun assignAddressToView(savedAddress: SavedAddress) {
        locationNameTextView.text = savedAddress.subAdminArea
        locationDetailsNameTextView.text =
            savedAddress.adminArea.plus(" ${savedAddress.countryName}")
    }


}