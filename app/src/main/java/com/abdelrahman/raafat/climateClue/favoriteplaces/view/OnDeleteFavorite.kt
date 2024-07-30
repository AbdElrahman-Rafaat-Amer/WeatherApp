package com.abdelrahman.raafat.climateClue.favoriteplaces.view

import com.abdelrahman.raafat.climateClue.model.FavoritePlaces

interface OnDeleteFavorite {
    fun deleteFromRoom(favoritePlaces: FavoritePlaces)
    fun showDetails(latitude: String, longitude: String, language: String)
}