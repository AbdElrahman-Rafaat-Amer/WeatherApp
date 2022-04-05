package com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import java.lang.IllegalArgumentException

class FavoritePlaceViewModelFactory(private val _irepo: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoritePlaceViewModel::class.java)) {
            FavoritePlaceViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}