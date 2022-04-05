package com.abdelrahman.rafaat.weatherapp.homeplaces.view


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.MainActivity
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModel
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils


//273.15

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
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
    private lateinit var viewModelFactory: CurrentPlaceViewModelFactory
    private lateinit var viewModel: CurrentPlaceViewModel

    private lateinit var visibilityConstrainLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var  moveAnimation : Animation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        initUI(view)
        viewModelFactory = CurrentPlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource.getInstance(view.context)
            )
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(CurrentPlaceViewModel::class.java)

        if (isInternetAvailable()) {
            viewModel.getWeatherFromNetwork(
                ConstantsValue.latitude,
                ConstantsValue.longitude,
                ConstantsValue.language
            )
        } else {
            Log.i(TAG, "onViewCreated: error in network")
            progressBar.visibility = GONE
            visibilityConstrainLayout.visibility = VISIBLE
            Toast.makeText(context, "Data will loaded from room", Toast.LENGTH_SHORT).show()

        }

        viewModel.weatherResponse.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onCreateView: observe $it")
            when (it) {
                is WeatherResponse -> {
                    assignDataToView(it)
                }
                else -> Toast.makeText(context, "Return is null $it", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun initUI(view: View) {
        progressBar = view.findViewById(R.id.loading_progressBar)
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
        weatherHourlyAdapter = WeatherHourlyAdapter(view.context)
        val hourlyManager = LinearLayoutManager(view.context)
        hourlyManager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyRecyclerView.layoutManager = hourlyManager
        hourlyRecyclerView.adapter = weatherHourlyAdapter


        dailyRecyclerView = view.findViewById(R.id.daily_recyclerView)
        weatherDailyAdapter = WeatherDailyAdapter(view.context, mainActivity)
        val dailyManager = LinearLayoutManager(view.context)
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

        moveAnimation = AnimationUtils.loadAnimation(requireContext(),
            R.anim.move);
        locationNameTextView.startAnimation(moveAnimation)
    }

    private fun isInternetAvailable(): Boolean {
        var isAvailable = false
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        isAvailable = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        isAvailable = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        isAvailable = true
                    }
                    else -> isAvailable = false
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        return isAvailable
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun assignDataToView(weatherResponse: WeatherResponse) {
        progressBar.visibility = GONE
        visibilityConstrainLayout.visibility = VISIBLE

        locationNameTextView.text = ConstantsValue.address!!.locality

        locationDetailsNameTextView.text =
            ConstantsValue.address!!.subAdminArea + " - " + ConstantsValue.address!!.adminArea + " - " + ConstantsValue.address!!.countryName
        currentDateTextView.text = formatDate(weatherResponse.current.dt)
        currentDayStatusTextView.text = weatherResponse.current.weather[0].description
        currentDayTemperatureTextView.text = getTemperature(weatherResponse.current.temp)

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/" + weatherResponse.current.weather[0].icon + "@2x.png")
            .into(currentDayImageView)
        weatherDailyAdapter.setList(weatherResponse.daily)
        weatherDailyAdapter.notifyDataSetChanged()
        weatherHourlyAdapter.setList(weatherResponse.hourly)
        weatherHourlyAdapter.notifyDataSetChanged()

        sunRiseTimeTextView.text = getTimeInHour(
            weatherResponse.current.sunrise,
            weatherResponse.timezone
        )
        sunSetTimeTextView.text = getTimeInHour(
            weatherResponse.current.sunset,
            weatherResponse.timezone
        )
        humidityTextView.text = DecimalFormat("#").format(weatherResponse.current.humidity) + " %"
        windSpeedTextView.text = getWindSpeed(weatherResponse)
        windDegreeTextView.text = DecimalFormat("##").format(weatherResponse.current.wind_deg)
        cloudTextView.text = DecimalFormat("#").format(weatherResponse.current.clouds) + " %"
        pressureTextView.text =
            DecimalFormat("#").format(weatherResponse.current.pressure) + " " + getString(R.string.pressure_unit)
        visibilityTextView.text =
            DecimalFormat("#").format(weatherResponse.current.visibility) + " " + getString(R.string.meter)
        ultravioletTextView.text = DecimalFormat("#.##").format(weatherResponse.current.uvi)
    }

    private fun getWindSpeed(weatherResponse: WeatherResponse): String {
        var windSpeed = ""
        windSpeed = when (ConstantsValue.windSpeedUnit) {
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
        var temperature = ""
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

    @RequiresApi(Build.VERSION_CODES.O)
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
        Log.i(TAG, "getTimeInHour: date " + date)
        val format = SimpleDateFormat("h:mm a", Locale(ConstantsValue.language))
        format.timeZone = TimeZone.getTimeZone(timeZone)
        Log.i(TAG, format.format(date))
        return format.format(date)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach: ")
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}