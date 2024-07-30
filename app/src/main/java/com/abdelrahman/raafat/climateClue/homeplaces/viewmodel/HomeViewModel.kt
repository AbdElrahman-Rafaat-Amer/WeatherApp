package com.abdelrahman.raafat.climateClue.homeplaces.viewmodel

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.*
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.database.ConcreteLocaleSource
import com.abdelrahman.raafat.climateClue.extension.getDayName
import com.abdelrahman.raafat.climateClue.homeplaces.view.HomeItem
import com.abdelrahman.raafat.climateClue.model.Repository
import com.abdelrahman.raafat.climateClue.model.RepositoryInterface
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import com.abdelrahman.raafat.climateClue.network.WeatherClient
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.LocaleHelper
import com.abdelrahman.raafat.climateClue.utils.TemperatureType
import com.abdelrahman.raafat.climateClue.utils.WindSpeedType
import com.abdelrahman.raafat.climateClue.utils.formatDate
import com.abdelrahman.raafat.climateClue.utils.getTimeInHour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {

    private var _iRepo: RepositoryInterface = Repository.getInstance(
        application.applicationContext,
        WeatherClient.getInstance(),
        ConcreteLocaleSource(application.applicationContext)
    )
    private val mContext by lazy {
        LocaleHelper.getLocalizedContext(application.applicationContext)
    }
    private var isGetAddressDone = false
    private var isGetWeatherDone = false

    private var _homeList = MutableLiveData<List<HomeItem>>()
    val homeList: LiveData<List<HomeItem>> = _homeList

    private var _weatherResponse = MutableLiveData<WeatherResponse?>()
    val weatherResponse: LiveData<WeatherResponse?> = _weatherResponse

    private var addressResponse: SavedAddress? = null
    private var weatherResponse2: WeatherResponse? = null

    init {
        getDataFromRoom()
        getStoredAddressFromRoom()
    }


    fun getWeatherFromNetwork(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.Main) {
                _weatherResponse.postValue(response)
                isGetWeatherDone = true
                weatherResponse2 = response
                setupHomeData()
            }
        }
    }

    private fun getAddress() {
        var address = SavedAddress("En", "Alex", "Alex", "Egypt")
        var subAdminArea = mContext.getString(R.string.undefined_place)
        try {
            val geocoder = Geocoder(application.applicationContext, Locale.getDefault())

            val addresses = geocoder.getFromLocation(
                ConstantsValue.latitude.toDouble(),
                ConstantsValue.longitude.toDouble(), 1
            )

            addresses?.get(0)?.subAdminArea?.let {
                subAdminArea = it
            }

            address = SavedAddress(
                ConstantsValue.language,
                subAdminArea,
                addresses?.get(0)?.adminArea ?: "", addresses?.get(0)?.countryName ?: ""
            )
            addressResponse = address
            isGetAddressDone = true
            setupHomeData()
        } catch (exception: IOException) {
            Log.e("Utils", "getAddress: exception-------------------- ${exception.message}")
        }
        insertAddressToRoom(address)
    }

    fun getDataFromRoom() {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromDataBase()
            withContext(Dispatchers.Main) {
                _weatherResponse.postValue(response)
                isGetWeatherDone = true
                weatherResponse2 = response
                setupHomeData()
            }

        }
    }

    private fun getStoredAddressFromRoom() {
        viewModelScope.launch {
            val response = _iRepo.getStoredPlace()
            withContext(Dispatchers.Main) {
                isGetAddressDone = true
                addressResponse = response
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

            weatherResponse2?.let {

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
                            dayName = day.dt.getDayName(),
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
                        title = mContext.getString(R.string.sunrise),
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
                        title = mContext.getString(R.string.sunset),
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
                        title = mContext.getString(R.string.humidity_cardView),
                        description = DecimalFormat("#").format(it.current.humidity).plus(" %")
                    )
                )

                //wind_speed
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_wind,
                        title = mContext.getString(R.string.wind_speed),
                        description = getWindSpeed(it)
                    )
                )

                //wind_degree
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_wind,
                        title = mContext.getString(R.string.wind_degree),
                        description = DecimalFormat("##").format(it.current.wind_deg)
                    )
                )

                //cloud
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_cloud,
                        title = mContext.getString(R.string.cloud),
                        description = DecimalFormat("#").format(it.current.clouds).plus(" %")
                    )
                )

                //pressure
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_pressure,
                        title = mContext.getString(R.string.pressure),
                        description = DecimalFormat("#").format(it.current.pressure)
                            .plus(" ${mContext.getString(R.string.pressure_unit)}")
                    )
                )

                //visibility
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_visibility,
                        title = mContext.getString(R.string.visibility),
                        description = DecimalFormat("#").format(it.current.visibility)
                            .plus(" ${mContext.getString(R.string.meter)}")
                    )
                )

                //ultraviolet
                homeItems.add(
                    HomeItem.DayInfoItem(
                        icon = R.drawable.ic_ultraviolet,
                        title = mContext.getString(R.string.ultraviolet),
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
                unit = TemperatureType.getLocalizedUnit(mContext, TemperatureType.Celsius.unit)
            }

            TemperatureType.Fahrenheit.unit -> {
                temperature = DecimalFormat("#").format(((temp - 273.15) * 1.8) + 32)
                unit = TemperatureType.getLocalizedUnit(mContext, TemperatureType.Fahrenheit.unit)
            }

            TemperatureType.Kelvin.unit -> {
                temperature = DecimalFormat("#").format(temp)
                unit = TemperatureType.getLocalizedUnit(mContext, TemperatureType.Kelvin.unit)
            }
        }
        return Pair(temperature, unit)
    }

    private fun getWindSpeed(weatherResponse: WeatherResponse): String {
        val windSpeed = when (ConstantsValue.windSpeedUnit) {
            WindSpeedType.MilePerHour.unit -> {
                DecimalFormat("#.##")
                    .format(weatherResponse.current.wind_speed * 3.6) + " " +
                        WindSpeedType.getLocalizedUnit(mContext, WindSpeedType.MilePerHour.unit)
            }

            else -> {
                DecimalFormat("#.##")
                    .format(weatherResponse.current.wind_speed) + " " +
                        WindSpeedType.getLocalizedUnit(mContext, WindSpeedType.MeterPerSecond.unit)
            }
        }
        return windSpeed
    }

    private fun insertAddressToRoom(address: SavedAddress) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _iRepo.insertAddressToRoom(address)
            }
        }
    }

    fun onInterconnectionChanged(isInternet: Boolean) {
        resetData()
        if (isInternet) {
            getAddress()
            getWeatherFromNetwork(
                ConstantsValue.latitude, ConstantsValue.longitude, ConstantsValue.language
            )
        } else {
            getDataFromRoom()
            getStoredAddressFromRoom()
        }
    }

    private fun resetData() {
        isGetAddressDone = false
        isGetWeatherDone = false
        addressResponse = null
        weatherResponse2 = null
    }

}