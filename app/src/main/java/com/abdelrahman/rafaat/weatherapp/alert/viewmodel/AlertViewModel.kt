package com.abdelrahman.rafaat.weatherapp.alert.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.SavedAlerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _alertResponse = MutableLiveData<List<SavedAlerts>>()
    val alertResponse: LiveData<List<SavedAlerts>> = _alertResponse


    fun getStoredAlerts() {
        viewModelScope.launch {
            val response = _iRepo.getStoredAlerts()
            withContext(Dispatchers.Main) {
                _alertResponse.postValue(response)
            }
        }
    }

    fun insertAlert(savedAlerts: SavedAlerts) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _iRepo.insertAlertToRoom(savedAlerts)
            }
        }
    }

    fun deleteAlertFromRoom(id: Int) {
        viewModelScope.launch {
            val response = _iRepo.deleteAlertFromRoom(id)
            withContext(Dispatchers.Main) {
                if (response > 0)
                    getStoredAlerts()
                else
                    Log.i("AlertFragment", "deleteAlertFromRoom: failed")
            }
        }
    }

}