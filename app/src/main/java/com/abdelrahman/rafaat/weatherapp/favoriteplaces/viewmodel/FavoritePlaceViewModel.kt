package com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritePlaceViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val TAG = "FavoritePlaceViewModel"
    private val _iRepo: RepositoryInterface = iRepo
    private var _favoritePlaces = MutableLiveData<List<FavoritePlaces>>()
    val favoritePlaces: LiveData<List<FavoritePlaces>> = _favoritePlaces

    private var _selectedFavoritePlaces = MutableLiveData<WeatherResponse>()
    val selectedFavoritePlaces: LiveData<WeatherResponse> = _selectedFavoritePlaces

    init {
        Log.i(TAG, "init: ")
    }

    fun getStoredFavoritePlaces() {
        viewModelScope.launch {
            val response = _iRepo.getFavoriteFromDataBase()
            withContext(Dispatchers.Main) {
                _favoritePlaces.postValue(response)
            }
        }
    }

    fun deleteFromRoom(favoritePlaces: FavoritePlaces) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _iRepo.removeFromFavorite(favoritePlaces)
            }
        }
    }

    fun getDetailsOfSelectedFavorite(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            var response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.Main) {
                _selectedFavoritePlaces.postValue(response)
                Log.i(
                    TAG,
                    "\n\n\n -------------------------------- \n\n\n getDetailsOfSelectedFavorite: response $response" +
                            " \n\n\n-------------------------------- \n\n\n"
                )
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: ")
    }

}