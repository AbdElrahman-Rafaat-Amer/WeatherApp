package com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel


import android.util.Log
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

    init {
        Log.i("Favorite", "init-----------------: ")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("Favorite", "onCleared-----------------: ")
    }

    fun getStoredFavoritePlaces() {
        viewModelScope.launch {
            val response = _iRepo.getFavoriteFromDataBase()
            withContext(Dispatchers.Main) {
                Log.i(
                    "Favorite",
                    "getStoredFavoritePlaces:response---------------------------> ${response.size}"
                )
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
                Log.i("Favorite", "delete ViewModel:response------------> $response")
                if (response > 0) {
                    Log.i("Favorite", "deleteFromRoom: success")
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