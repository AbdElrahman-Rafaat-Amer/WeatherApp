package com.abdelrahman.raafat.climateClue.ui.home.viewmodel

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.database.ConcreteLocaleSource
import com.abdelrahman.raafat.climateClue.extension.toAbbreviatedDayName
import com.abdelrahman.raafat.climateClue.extension.toFullDateString
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem
import com.abdelrahman.raafat.climateClue.model.Daily
import com.abdelrahman.raafat.climateClue.model.DayInfo
import com.abdelrahman.raafat.climateClue.model.Repository
import com.abdelrahman.raafat.climateClue.model.RepositoryInterface
import com.abdelrahman.raafat.climateClue.model.SavedAddress
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import com.abdelrahman.raafat.climateClue.network.WeatherClient
import com.abdelrahman.raafat.climateClue.ui.timetable.TimeTableItem
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.LocaleHelper
import com.abdelrahman.raafat.climateClue.utils.TemperatureType
import com.abdelrahman.raafat.climateClue.utils.WindSpeedType
import com.abdelrahman.raafat.climateClue.utils.formatDailyTemperatureRange
import com.abdelrahman.raafat.climateClue.utils.formatDate
import com.abdelrahman.raafat.climateClue.utils.formatTime
import com.abdelrahman.raafat.climateClue.utils.getMoonPhase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.DecimalFormat
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

    private var _timeTableList = MutableLiveData<List<TimeTableItem>>()
    val timeTableList: LiveData<List<TimeTableItem>> = _timeTableList

    private var addressResponse: SavedAddress? = null
    private var weatherResponse: WeatherResponse? = null

    init {
        getDataFromRoom()
        getStoredAddressFromRoom()
    }

    fun onInterconnectionChanged(isInternet: Boolean) {
        resetData()

        if (isInternet) {
            fetchWeatherDataOnline()
            fetchAddressIfNeeded()
        } else {
            fetchWeatherDataOffline()
            fetchStoredAddressIfNeeded()
        }
    }

    private fun fetchWeatherDataOnline() {
        try {
            getWeatherFromNetwork(
                ConstantsValue.latitude,
                ConstantsValue.longitude,
                ConstantsValue.language
            )
        } catch (e: Exception) {
            // Handle network exceptions or log the error
            Log.e("NetworkError", "Failed to fetch weather data: ${e.message}")
        }
    }

    private fun fetchWeatherDataOffline() {
        try {
            getDataFromRoom()
        } catch (e: Exception) {
            // Handle data retrieval exceptions or log the error
            Log.e("DataRetrievalError", "Failed to fetch data from Room: ${e.message}")
        }
    }

    private fun fetchAddressIfNeeded() {
        try {
            getAddress()
        } catch (e: Exception) {
            // Handle address fetching exceptions or log the error
            Log.e("AddressError", "Failed to fetch address: ${e.message}")
        }
    }

    private fun fetchStoredAddressIfNeeded() {
        try {
            getStoredAddressFromRoom()
        } catch (e: Exception) {
            // Handle stored address retrieval exceptions or log the error
            Log.e("StoredAddressError", "Failed to fetch stored address: ${e.message}")
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

    private fun getWeatherFromNetwork(
        latitude: String,
        longitude: String,
        language: String
    ) {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.Main) {
                isGetWeatherDone = true
                weatherResponse = response
                setupHomeData()
            }
        }
    }

    private fun getDataFromRoom() {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromDataBase()
            withContext(Dispatchers.Main) {
                isGetWeatherDone = true
                weatherResponse = response
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
                            dayName = day.dt.toAbbreviatedDayName(),
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
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_sunny,
                            title = mContext.getString(R.string.sunrise),
                            description = formatTime(
                                it.current.sunrise,
                                it.timezone
                            )
                        )
                    )
                )

                //sunset
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_ocean,
                            title = mContext.getString(R.string.sunset),
                            description = formatTime(
                                it.current.sunset,
                                it.timezone
                            )
                        )
                    )
                )

                //humidity
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_humidity,
                            title = mContext.getString(R.string.humidity_cardView),
                            description = DecimalFormat("#").format(it.current.humidity).plus(" %")
                        )
                    )
                )

                //wind_speed
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_wind,
                            title = mContext.getString(R.string.wind_speed),
                            description = getWindSpeed(it.current.wind_speed)
                        )
                    )
                )

                //wind_degree
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_wind,
                            title = mContext.getString(R.string.wind_degree),
                            description = DecimalFormat("##").format(it.current.wind_deg)
                        )
                    )
                )

                //cloud
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_cloud,
                            title = mContext.getString(R.string.cloud),
                            description = DecimalFormat("#").format(it.current.clouds).plus(" %")
                        )
                    )
                )

                //pressure
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_pressure,
                            title = mContext.getString(R.string.pressure),
                            description = DecimalFormat("#").format(it.current.pressure)
                                .plus(" ${mContext.getString(R.string.pressure_unit)}")
                        )
                    )
                )

                //visibility
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_visibility,
                            title = mContext.getString(R.string.visibility),
                            description = DecimalFormat("#").format(it.current.visibility)
                                .plus(" ${mContext.getString(R.string.meter)}")
                        )
                    )
                )

                //ultraviolet
                homeItems.add(
                    HomeItem.DayInfoItem(
                        dayInfo = DayInfo(
                            iconFromDrawable = R.drawable.ic_ultraviolet,
                            title = mContext.getString(R.string.ultraviolet),
                            description = DecimalFormat("#.##").format(it.current.uvi)
                        )
                    )
                )
            }

            _homeList.postValue(homeItems)
        }
    }

    fun setupTimeTableData() {
        val timeTableItems = arrayListOf<TimeTableItem>()
        weatherResponse?.let { response ->
            response.daily.forEach { day ->


                timeTableItems.add(
                    TimeTableItem.DayItem(
                        dayName = day.dt.toFullDateString(),
                        dayInfo = setupDayInfoData(day, response.timezone)
                    )
                )
            }
        }

        _timeTableList.postValue(timeTableItems)
    }

    private fun setupDayInfoData(day: Daily, timezone: String): List<TimeTableItem.DayInfoItem> {
        val list = arrayListOf<TimeTableItem.DayInfoItem>()
        //SunRise
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_sunny,
                    title = mContext.getString(R.string.sunrise),
                    description = formatTime(day.sunrise, timezone)
                )
            )
        )

        //SunSet
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_ocean,
                    title = mContext.getString(R.string.sunrise),
                    description = formatTime(day.sunrise, timezone)
                )
            )
        )

        //moon phases
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_moon_phases,
                    title = mContext.getString(R.string.moon_phase),
                    description = getMoonPhase(day, mContext)
                )
            )
        )

        //temperature
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_temperature,
                    title = mContext.getString(R.string.temperature),
                    description = formatDailyTemperatureRange(day, mContext)
                )
            )
        )

        //weather
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconURL = ConstantsValue.IMAGE_URL + day.weather[0].icon + "@2x.png",
                    iconFromDrawable = R.drawable.ic_sunny,
                    title = mContext.getString(R.string.weather_description),
                    description = day.weather[0].description
                )
            )
        )

        //rain
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_rain,
                    title = mContext.getString(R.string.probability_precipitation),
                    description = DecimalFormat("#").format((day.pop * 100)).plus(" %")
                )
            )
        )

        //cloud
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_cloud,
                    title = mContext.getString(R.string.cloud),
                    description = DecimalFormat("#").format(day.clouds).plus(" %")
                )
            )
        )

        //pressure
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_pressure,
                    title = mContext.getString(R.string.pressure),
                    description = DecimalFormat("#").format(day.pressure)
                        .plus(" ${mContext.getString(R.string.pressure_unit)}")
                )
            )
        )

        //ultraviolet
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_ultraviolet,
                    title = mContext.getString(R.string.ultraviolet),
                    description = DecimalFormat("#.##").format(day.uvi)
                )
            )
        )

        //humidity
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_humidity,
                    title = mContext.getString(R.string.humidity_cardView),
                    description = DecimalFormat("#").format(day.humidity).plus(" %")
                )
            )
        )

        //wind speed
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_wind,
                    title = mContext.getString(R.string.wind_speed),
                    description = getWindSpeed(day.wind_speed)
                )
            )
        )

        // wind degree
        list.add(
            TimeTableItem.DayInfoItem(
                dayInfo = DayInfo(
                    iconFromDrawable = R.drawable.ic_wind,
                    title = mContext.getString(R.string.wind_degree),
                    description = DecimalFormat("##").format(day.wind_deg)
                )
            )
        )

        return list
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

    private fun getWindSpeed(windSpeed: Double): String {
        return when (ConstantsValue.windSpeedUnit) {
            WindSpeedType.MilePerHour.unit -> {
                DecimalFormat("#.##")
                    .format(windSpeed * 3.6) + " " +
                        WindSpeedType.getLocalizedUnit(mContext, WindSpeedType.MilePerHour.unit)
            }

            else -> {
                DecimalFormat("#.##")
                    .format(windSpeed) + " " +
                        WindSpeedType.getLocalizedUnit(mContext, WindSpeedType.MeterPerSecond.unit)
            }
        }
    }

    private fun insertAddressToRoom(address: SavedAddress) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _iRepo.insertAddressToRoom(address)
            }
        }
    }

    private fun resetData() {
        isGetAddressDone = false
        isGetWeatherDone = false
        addressResponse = null
        weatherResponse = null
    }

}