package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces

interface OnDeleteFavorite {
    fun deleteFromRoom(favoritePlaces: FavoritePlaces)
    fun showDetails(latitude: String, longitude: String, language: String)
}