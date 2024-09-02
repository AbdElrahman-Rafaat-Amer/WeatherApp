package com.abdelrahman.raafat.climateClue.favorite.view

import com.abdelrahman.raafat.climateClue.model.FavoritePlaces

interface OnDeleteFavorite {
    fun deleteFromRoom(favoritePlaces: FavoritePlaces)
    fun showDetails(latitude: String, longitude: String, language: String)
}