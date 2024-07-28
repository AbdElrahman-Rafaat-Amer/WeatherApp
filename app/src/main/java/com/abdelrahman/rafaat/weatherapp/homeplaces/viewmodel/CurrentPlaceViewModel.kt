package com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeItem
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentPlaceViewModel(application: Application) : AndroidViewModel(application) {

    private var _iRepo: RepositoryInterface


    private var _homeList = MutableLiveData<List<HomeItem>>()
    val homeList: LiveData<List<HomeItem>> = _homeList

    private var _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse

    private var _addressResponse = MutableLiveData<SavedAddress>()
    val addressResponse: LiveData<SavedAddress> = _addressResponse

    init {
        _iRepo =
            Repository.getInstance(
                application.applicationContext,
                WeatherClient.getInstance(),
                ConcreteLocaleSource(application.applicationContext)
            )
    }


    fun getWeatherFromNetwork(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.Main) {
                _weatherResponse.postValue(response)

            }

        }
    }

    fun getDataFromRoom() {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromDataBase()
            withContext(Dispatchers.Main) {
                _weatherResponse.postValue(response!!)
            }

        }
    }

    fun getStoredAddressFromRoom() {
        viewModelScope.launch {
            val response = _iRepo.getStoredPlace()
            withContext(Dispatchers.Main) {
                _addressResponse.postValue(response)
            }

        }
    }


    fun insertAddressToRoom(address: SavedAddress) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _iRepo.insertAddressToRoom(address)
            }

        }
    }

}