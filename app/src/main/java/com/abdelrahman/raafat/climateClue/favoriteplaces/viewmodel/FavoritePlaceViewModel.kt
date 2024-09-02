package com.abdelrahman.raafat.climateClue.favoriteplaces.viewmodel


import android.util.Log
import androidx.lifecycle.*
import com.abdelrahman.raafat.climateClue.model.FavoritePlaces
import com.abdelrahman.raafat.climateClue.model.RepositoryInterface
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritePlaceViewModel(private var _iRepo: RepositoryInterface) : ViewModel() {

    private var _favoritePlaces = MutableLiveData<List<FavoritePlaces>>()
    val favoritePlaces: LiveData<List<FavoritePlaces>> = _favoritePlaces

    private var _selectedFavoritePlaces = MutableLiveData<WeatherResponse>()
    val selectedFavoritePlaces: LiveData<WeatherResponse> = _selectedFavoritePlaces


    fun getStoredFavoritePlaces() {
        viewModelScope.launch {
            val response = _iRepo.getFavoriteFromDataBase()
            withContext(Dispatchers.Main) {
                _favoritePlaces.postValue(response)
            }
        }
    }

    fun insertToFavorite(favoritePlaces: FavoritePlaces) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _iRepo.insertToFavorite(favoritePlaces)
            }
        }
    }

    fun deleteFromRoom(favoritePlaces: FavoritePlaces) {
        viewModelScope.launch {
            val response = _iRepo.removeFromFavorite(favoritePlaces)
            withContext(Dispatchers.Main) {
                if (response > 0) {
                    getStoredFavoritePlaces()
                } else {
                    Log.i("Favorite", "deleteFromRoom: failed")
                }

            }
        }
    }

    fun getDetailsOfSelectedFavorite(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.IO) {
                _selectedFavoritePlaces.postValue(response)
            }
        }
    }
}