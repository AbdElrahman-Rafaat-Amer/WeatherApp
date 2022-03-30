package com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentPlaceViewModel(private val _irepo: RepositoryInterface): ViewModel() {
    init {
        getWeatherFromNetwork()
    }

    private var _weatherResponse: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weatherResponse: LiveData<WeatherResponse> = _weatherResponse

    private fun getWeatherFromNetwork() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _irepo.getWeatherFromNetwork()
            }

        }
    }

}