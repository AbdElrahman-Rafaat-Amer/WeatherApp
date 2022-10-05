package com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel


import androidx.lifecycle.*
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import com.abdelrahman.rafaat.weatherapp.model.RepositoryInterface
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
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
            withContext(Dispatchers.Main) {
                _iRepo.removeFromFavorite(favoritePlaces)
            }
        }
    }

    fun getDetailsOfSelectedFavorite(latitude: String, longitude: String, language: String) {
        viewModelScope.launch {
            val response = _iRepo.getWeatherFromNetwork(latitude, longitude, language)
            withContext(Dispatchers.Main) {
                _selectedFavoritePlaces.postValue(response)
            }
        }
    }
}