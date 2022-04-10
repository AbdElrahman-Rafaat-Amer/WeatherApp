package com.abdelrahman.rafaat.weatherapp.maps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val TAG = "MapViewModel"
    private val _iRepo: RepositoryInterface = iRepo

    init {
        Log.i(TAG, "init: ")
    }

    fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _iRepo.insertToFavorite(favoritePlaces)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: ")
    }

}