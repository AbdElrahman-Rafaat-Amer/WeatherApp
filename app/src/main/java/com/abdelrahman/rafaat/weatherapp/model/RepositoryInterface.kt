package com.abdelrahman.rafaat.weatherapp.model


interface RepositoryInterface {

    //Network
    suspend fun getWeatherFromNetwork(
        latitude: String,
        longitude: String,
        language: String
    ): WeatherResponse

    //Room

    //Current
    suspend fun getWeatherFromDataBase(): WeatherResponse?
    suspend fun insertCurrentDataToRoom(weatherResponse: WeatherResponse)


    //Address
    suspend fun getStoredPlace(): SavedAddress?
    suspend fun insertAddressToRoom(address: SavedAddress)


    //Favorites
    suspend fun getFavoriteFromDataBase(): List<FavoritePlaces>
    suspend fun insertToFavorite(favoritePlaces: FavoritePlaces)
    suspend fun removeFromFavorite(favoritePlaces: FavoritePlaces) : Int


    //Alerts
    suspend fun insertAlertToRoom(savedAlerts: SavedAlerts): Long
    suspend fun getStoredAlerts(): List<SavedAlerts>
    suspend fun deleteAlertFromRoom(id: Int): Int
    suspend fun getAlertFromRoom(id: Int): SavedAlerts


}