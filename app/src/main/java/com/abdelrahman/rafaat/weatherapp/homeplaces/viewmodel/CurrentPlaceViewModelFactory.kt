package com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface

class CurrentPlaceViewModelFactory(private val _irepo: RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentPlaceViewModel(_irepo) as T
    }
}