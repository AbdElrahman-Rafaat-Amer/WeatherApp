package com.abdelrahman.raafat.climateClue.favoriteplaces.view

import android.os.Bundle
import com.abdelrahman.raafat.climateClue.utils.getAddress
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.database.ConcreteLocaleSource
import com.abdelrahman.raafat.climateClue.databinding.FragmentHomeBinding
import com.abdelrahman.raafat.climateClue.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.raafat.climateClue.favoriteplaces.viewmodel.FavoritePlaceViewModelFactory
import com.abdelrahman.raafat.climateClue.homeplaces.view.WeatherDailyAdapter
import com.abdelrahman.raafat.climateClue.homeplaces.view.WeatherHourlyAdapter
import com.abdelrahman.raafat.climateClue.model.Repository
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import com.abdelrahman.raafat.climateClue.network.WeatherClient
import com.abdelrahman.raafat.climateClue.utils.formatDate
import com.abdelrahman.raafat.climateClue.utils.getTimeInHour
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class FavoriteDetailsFragment : Fragment() {

    private lateinit var viewModel: FavoritePlaceViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter
    private lateinit var weatherDailyAdapter: WeatherDailyAdapter


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
        initViewModel()
        getData()
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

    private fun initViewModel() {
        val viewModelFactory = FavoritePlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(requireContext())
            )
        )
        viewModel =
            ViewModelProvider(this, viewModelFactory)[FavoritePlaceViewModel::class.java]
    }

    private fun getData() {
        val latitude = arguments?.getString("LAT") as String
        val longitude = arguments?.getString("LONG") as String
        viewModel.getDetailsOfSelectedFavorite(latitude, longitude, ConstantsValue.language)
    }

    private fun observeViewModel() {
        viewModel.selectedFavoritePlaces.observe(viewLifecycleOwner) {
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

    private fun getWindSpeed(weatherResponse: WeatherResponse): String {
        val windSpeed: String = when (ConstantsValue.windSpeedUnit) {
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
        binding.locationNameTextView.text = savedAddress.subAdminArea
        binding.locationDetailsNameTextView.text =
            savedAddress.adminArea.plus(" ${savedAddress.countryName}")
    }


}