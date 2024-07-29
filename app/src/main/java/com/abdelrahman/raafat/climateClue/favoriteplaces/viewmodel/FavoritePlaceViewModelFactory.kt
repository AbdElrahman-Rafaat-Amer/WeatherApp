package com.abdelrahman.raafat.climateClue.favoriteplaces.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdelrahman.raafat.climateClue.model.RepositoryInterface
import java.lang.IllegalArgumentException

class FavoritePlaceViewModelFactory(private val _repo: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoritePlaceViewModel::class.java)) {
            FavoritePlaceViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}
