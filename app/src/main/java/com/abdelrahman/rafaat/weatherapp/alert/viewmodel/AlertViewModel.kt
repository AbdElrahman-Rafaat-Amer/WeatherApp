package com.abdelrahman.rafaat.weatherapp.alert.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val TAG = "AlertViewModel"
    private val _iRepo: RepositoryInterface = iRepo
    private var _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse


    init {
        Log.i(TAG, "init: ")
    }


    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: ")
        Log.i(TAG, "_weatherResponse: $_weatherResponse")
        Log.i(TAG, "weatherResponse1245: $weatherResponse")
    }

}