package com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.SavedAddress
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentPlaceViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val TAG = "CurrentPlaceViewModel"
    private val _iRepo: RepositoryInterface = iRepo
    private var _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse

    private var _addressResponse = MutableLiveData<SavedAddress>()
    val addressResponse: LiveData<SavedAddress> = _addressResponse

    init {
        Log.i(TAG, "init: ")
    }


    fun getWeatherFromNetwork(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            Log.i(TAG, "getWeatherFromNetwork: longitude ---> " + longitude)
            Log.i(TAG, "getWeatherFromNetwork: latitude ---> " + latitude)
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.Main) {
                Log.i(TAG, "getWeatherFromNetwork: $weatherResponse")
                _weatherResponse.postValue(response)

            }

        }
    }

    fun getDataFromRoom() {
        Log.i(TAG, "getDataFromRoom: getFromRoom---------------> ")
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromDataBase()
            withContext(Dispatchers.Main) {
                Log.i(TAG, "getWeatherFromNetwork: $response")
                _weatherResponse.postValue(response!!)
            }

        }
    }

    fun getStoredAddressFromRoom() {
        Log.i(TAG, "getStoredAddressFromRoom: getFromRoom---------------> ")
        viewModelScope.launch {
            val response = _iRepo.getStoredPlace()
            withContext(Dispatchers.Main) {
                Log.i(TAG, "getStoredAddressFromRoom: $response")
                _addressResponse.postValue(response)

            }

        }
    }


    fun insertAddressToRoom(address: SavedAddress) {
        Log.i(TAG, "getStoredAddressFromRoom: getFromRoom---------------> ")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
               _iRepo.insertAddressToRoom(address)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: ")
        Log.i(TAG, "_weatherResponse: $_weatherResponse")
        Log.i(TAG, "weatherResponse1245: $weatherResponse")
    }

}