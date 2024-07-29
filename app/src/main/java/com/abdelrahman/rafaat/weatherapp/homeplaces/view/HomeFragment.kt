package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.databinding.FragmentHomeBinding
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModel
import com.abdelrahman.rafaat.weatherapp.model.*
import com.abdelrahman.rafaat.weatherapp.utils.*
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter
    private lateinit var weatherDailyAdapter: WeatherDailyAdapter
    private val viewModel: CurrentPlaceViewModel by activityViewModels()
    private var isInternet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        checkInternet()
        observeViewModel()

    }

    private fun initUI() {
        weatherHourlyAdapter = WeatherHourlyAdapter()
        val hourlyManager = LinearLayoutManager(requireContext())
        hourlyManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.hourlyRecyclerView.layoutManager = hourlyManager
        binding.hourlyRecyclerView.adapter = weatherHourlyAdapter

        weatherDailyAdapter = WeatherDailyAdapter()
        val dailyManager = LinearLayoutManager(requireContext())
        dailyManager.orientation = LinearLayoutManager.VERTICAL
        binding.dailyRecyclerView.layoutManager = dailyManager
        binding.dailyRecyclerView.adapter = weatherDailyAdapter

    }

    private fun checkInternet() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                isInternet = true
                getAddress()
                viewModel.getWeatherFromNetwork(
                    ConstantsValue.latitude, ConstantsValue.longitude, ConstantsValue.language
                )
            } else {
                binding.loadingAnimationView.visibility = GONE
                binding.visibilityConstrainLayout.visibility = VISIBLE
                viewModel.getDataFromRoom()
                viewModel.getStoredAddressFromRoom()
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (!isInternet) {
                viewModel.getDataFromRoom()
                viewModel.getStoredAddressFromRoom()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.weatherResponse.observe(viewLifecycleOwner) {
            when (it) {
                is WeatherResponse -> {
                    assignDataToView(it)
                }
                else -> Toast.makeText(context, "Return is null $it", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.addressResponse.observe(viewLifecycleOwner) {
            when (it) {
                is SavedAddress -> {
                    assignAddressToView(it)
                }
                else -> Toast.makeText(context, "Return is null $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun assignDataToView(weatherResponse: WeatherResponse) {
        binding.loadingAnimationView.visibility = GONE
        binding.visibilityConstrainLayout.visibility = VISIBLE

        binding.currentDateTextView.text = formatDate(weatherResponse.current.dt)
        binding.currentDayStatusTextView.text = weatherResponse.current.weather[0].description
        binding.currentDayTemperatureTextView.text = getTemperature(weatherResponse.current.temp)

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/" + weatherResponse.current.weather[0].icon + "@2x.png")
            .into(binding.currentDayImageView)
        weatherDailyAdapter.setList(weatherResponse.daily)
        weatherHourlyAdapter.setList(weatherResponse.hourly)


        binding.sunRiseTimeTextView.text = getTimeInHour(
            weatherResponse.current.sunrise,
            weatherResponse.timezone
        )
        binding.sunSetTimeTextView.text = getTimeInHour(
            weatherResponse.current.sunset,
            weatherResponse.timezone
        )
        binding.humidityTextView.text =
            DecimalFormat("#").format(weatherResponse.current.humidity).plus(" %")
        binding.windSpeedTextView.text = getWindSpeed(weatherResponse)
        binding.windDegreeTextView.text =
            DecimalFormat("##").format(weatherResponse.current.wind_deg)
        binding.cloudTextView.text =
            DecimalFormat("#").format(weatherResponse.current.clouds).plus(" %")
        binding.pressureTextView.text =
            DecimalFormat("#").format(weatherResponse.current.pressure)
                .plus(" ${getString(R.string.pressure_unit)}")
        binding.visibilityTextView.text =
            DecimalFormat("#").format(weatherResponse.current.visibility)
                .plus(" ${getString(R.string.meter)}")
        binding.ultravioletTextView.text = DecimalFormat("#.##").format(weatherResponse.current.uvi)

    }

    private fun getAddress() {
        val address = getAddress(
            ConstantsValue.latitude.toDouble(),
            ConstantsValue.longitude.toDouble(),
            requireContext()
        )
        assignAddressToView(
            address
        )
        viewModel.insertAddressToRoom(address)
    }

    private fun assignAddressToView(savedAddress: SavedAddress) {
        binding.locationNameTextView.text = savedAddress.subAdminArea
        binding.locationDetailsNameTextView.text =
            savedAddress.adminArea.plus(" -  ${savedAddress.countryName}")
    }

    private fun getWindSpeed(weatherResponse: WeatherResponse): String {
        val windSpeed = when (ConstantsValue.windSpeedUnit) {
            "M/H" -> DecimalFormat("#.##").format(weatherResponse.current.wind_speed * 3.6) + " " + getString(
                R.string.wind_speed_unit_MH
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
            "celsius" -> {
                temperature = DecimalFormat("#").format(temp - 273.15)
                binding.currentDayTemperatureUnitTextView.text =
                    resources.getString(R.string.temperature_celsius_unit)
            }
            "fahrenheit" -> {
                temperature = DecimalFormat("#").format(((temp - 273.15) * 1.8) + 32)
                binding.currentDayTemperatureUnitTextView.text =
                    resources.getString(R.string.temperature_fahrenheit_unit)
            }
            else -> {
                temperature = DecimalFormat("#").format(temp)
                binding.currentDayTemperatureUnitTextView.text =
                    resources.getString(R.string.temperature_kelvin_unit)
            }
        }
        return temperature
    }



}