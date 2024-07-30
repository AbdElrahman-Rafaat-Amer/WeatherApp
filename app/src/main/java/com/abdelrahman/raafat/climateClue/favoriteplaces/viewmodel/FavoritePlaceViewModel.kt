package com.abdelrahman.raafat.climateClue.favoriteplaces.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.database.ConcreteLocaleSource
import com.abdelrahman.raafat.climateClue.model.FavoritePlaces
import com.abdelrahman.raafat.climateClue.model.Repository
import com.abdelrahman.raafat.climateClue.model.RepositoryInterface
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import com.abdelrahman.raafat.climateClue.network.WeatherClient
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.formatDate
import com.abdelrahman.raafat.climateClue.utils.getTimeInHour
import com.abdelrahman.raafat.climateClue.homeplaces.view.HomeItem
import com.abdelrahman.raafat.climateClue.utils.TemperatureType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class FavoritePlaceViewModel(private val application: Application) : AndroidViewModel(application) {

    private var _iRepo: RepositoryInterface = Repository.getInstance(
        application.applicationContext,
        WeatherClient.getInstance(),
        ConcreteLocaleSource(application.applicationContext)
    )


    private var _favoritePlaces = MutableLiveData<List<FavoritePlaces>>()
    val favoritePlaces: LiveData<List<FavoritePlaces>> = _favoritePlaces

    private var _selectedFavoritePlaces = MutableLiveData<WeatherResponse>()
    val selectedFavoritePlaces: LiveData<WeatherResponse> = _selectedFavoritePlaces

    private var _homeList = MutableLiveData<List<HomeItem>>()
    val homeList: LiveData<List<HomeItem>> = _homeList

    private var weatherResponse: WeatherResponse? = null
    private var addressResponse: SavedAddress? = null
    private var isGetAddressDone = false
    private var isGetWeatherDone = false

    fun getStoredFavoritePlaces() {
        viewModelScope.launch {
            val response = _iRepo.getFavoriteFromDataBase()
            withContext(Dispatchers.Main) {
                _favoritePlaces.postValue(response)
            }
        }
    }

    fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _iRepo.insertToFavorite(favoritePlaces)
            }
        }
    }

    fun deleteFromRoom(favoritePlaces: FavoritePlaces) {
        viewModelScope.launch {
            val response = _iRepo.removeFromFavorite(favoritePlaces)
            withContext(Dispatchers.Main) {
                if (response > 0) {
                    getStoredFavoritePlaces()
                } else {
                    Log.i("Favorite", "deleteFromRoom: failed")
                }

            }
        }
    }

    fun getDetailsOfSelectedFavorite(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.IO) {
                _selectedFavoritePlaces.postValue(response)
                isGetWeatherDone = true
                weatherResponse = response
                setupHomeData()
            }
        }
    }
    private fun setupHomeData() {
        val homeItems = arrayListOf<HomeItem>()
        if (isGetAddressDone && isGetWeatherDone) {
            addressResponse?.let {
                homeItems.add(HomeItem.TitleItem(it.subAdminArea, isTextLarge = true))
                val locationDetails = it.adminArea.plus(" -  ${it.countryName}")
                homeItems.add(HomeItem.TitleItem(locationDetails, isTextSmall = true))
            }

            weatherResponse?.let {

                //Date
                val date = formatDate(it.current.dt)
                homeItems.add(HomeItem.TitleItem(date, isBold = true))

                //Weather Status
                val temperature = getTemperature(it.current.temp)
                homeItems.add(
                    HomeItem.StatusItem(
                        status = it.current.weather[0].description,
                        temperature = temperature.first,
                        temperatureUnit = temperature.second,
                        iconURL = ConstantsValue.IMAGE_URL + it.current.weather[0].icon + "@2x.png",
                        iconPlaceHolder = R.drawable.ic_sunny
                    )
                )

                //Hourly
                homeItems.add(HomeItem.HourlyItem(it.hourly))

                //Daily
                it.daily.isNotEmpty()
                it.daily.forEach { day ->
                    val dayTemperature = getTemperature(day.temp.day)
                    homeItems.add(
                        HomeItem.DailyItem(
                            dayName = getNameOfDay(day.dt),
                            dayStatus = day.weather[0].description,
                            dayTemperature = dayTemperature.first + " " + dayTemperature.second,
                            iconURL = ConstantsValue.IMAGE_URL + day.weather[0].icon + "@2x.png",
                            iconPlaceHolder = R.drawable.ic_sunny
                        )
                    )
                }

                //DayInfo

                //sunrise
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_sunny,
                        title = application.applicationContext.getString(R.string.sunrise),
                        description = getTimeInHour(
                            it.current.sunrise,
                            it.timezone
                        )
                    )
                )

                //sunset
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_ocean,
                        title = application.applicationContext.getString(R.string.sunset),
                        description = getTimeInHour(
                            it.current.sunset,
                            it.timezone
                        )
                    )
                )

                //humidity
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_humidity,
                        title = application.applicationContext.getString(R.string.humidity_cardView),
                        description = DecimalFormat("#").format(it.current.humidity).plus(" %")
                    )
                )

                //wind_speed
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_wind,
                        title = application.applicationContext.getString(R.string.wind_speed),
                        description = getWindSpeed(it)
                    )
                )

                //wind_degree
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_wind,
                        title = application.applicationContext.getString(R.string.wind_degree),
                        description = DecimalFormat("##").format(it.current.wind_deg)
                    )
                )

                //cloud
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_cloud,
                        title = application.applicationContext.getString(R.string.cloud),
                        description = DecimalFormat("#").format(it.current.clouds).plus(" %")
                    )
                )

                //pressure
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_pressure,
                        title = application.applicationContext.getString(R.string.pressure),
                        description = DecimalFormat("#").format(it.current.pressure)
                            .plus(" ${application.applicationContext.getString(R.string.pressure_unit)}")
                    )
                )

                //visibility
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_visibility,
                        title = "application.applicationContext.getString(R.string.visibility)",
                        description = DecimalFormat("#").format(it.current.visibility)
                            .plus(" ${application.applicationContext.getString(R.string.meter)}")
                    )
                )

                //ultraviolet
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_ultraviolet,
                        title = "application.applicationContext.getString(R.string.ultraviolet)",
                        description = DecimalFormat("#.##").format(it.current.uvi)
                    )
                )
            }

            _homeList.postValue(homeItems)
        }
    }

    private fun getTemperature(temp: Double): Pair<String, String> {
        var temperature = ""
        var unit = ""
        when (ConstantsValue.tempUnit) {
            TemperatureType.Celsius.unit -> {
                temperature = DecimalFormat("#").format(temp - 273.15)
                unit =
                    application.applicationContext.resources.getString(R.string.temperature_celsius_unit)
            }

            TemperatureType.Fahrenheit.unit -> {
                temperature = DecimalFormat("#").format(((temp - 273.15) * 1.8) + 32)
                unit =
                    application.applicationContext.resources.getString(R.string.temperature_fahrenheit_unit)
            }

            TemperatureType.Kelvin.unit -> {
                temperature = DecimalFormat("#").format(temp)
                unit =
                    application.applicationContext.resources.getString(R.string.temperature_kelvin_unit)
            }
        }
        return Pair(temperature, unit)
    }

    private fun getNameOfDay(milliSeconds: Long): String {
        return SimpleDateFormat("EE").format(milliSeconds * 1000)
    }

    private fun getWindSpeed(weatherResponse: WeatherResponse): String {
        val windSpeed = when (ConstantsValue.windSpeedUnit) {
            "M/H" -> DecimalFormat("#.##").format(weatherResponse.current.wind_speed * 3.6) + " " + application.applicationContext.getString(
                R.string.wind_speed_unit_MH
            )

            else -> DecimalFormat("#.##").format(weatherResponse.current.wind_speed) + " " + application.applicationContext.getString(
                R.string.wind_speed_unit_MS
            )
        }
        return windSpeed
    }

}