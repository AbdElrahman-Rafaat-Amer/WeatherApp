package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import java.util.*

class FavoritePlacesAdapter(context: Context, fragment: OnDeleteFavorite) :
    RecyclerView.Adapter<FavoritePlacesAdapter.ViewHolder>() {

    private val TAG = "FavoritePlacesAdapter"
    private var context = context
    private var favorites: List<FavoritePlaces> = ArrayList()
    private var fragment: OnDeleteFavorite = fragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritePlacesAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        var favoritePlace = favorites[position]
        holder.nameOfFavoritePlace.text = favoritePlace.selectedPlaces
        holder.dateOfFavoritePlace.text = favoritePlace.selectedDate
        holder.deleteFavoritePlace.setOnClickListener {
            Toast.makeText(context, "deleteFavoritePlace success", Toast.LENGTH_SHORT).show()
            fragment.deleteFromRoom(favoritePlace)
        }

        holder.showDetailsOfFavorite.setOnClickListener {
            Toast.makeText(context, "showDetailsOfFavorite success", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "onBindViewHolder: " + Locale.getDefault().toLanguageTag())
            fragment.showDetails(
                favoritePlace.lat.toString(),
                favoritePlace.lng.toString(),
                ConstantsValue.language
            )
        }

    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun setList(favorites: List<FavoritePlaces>) {
        this.favorites = favorites
        Log.i(TAG, "setList: hours" + favorites.size)
        Log.i(TAG, "setList: this.hours " + this.favorites.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameOfFavoritePlace: TextView = itemView.findViewById(R.id.name_of_favorite_place)
        var dateOfFavoritePlace: TextView = itemView.findViewById(R.id.date_added_to_favorite)
        var deleteFavoritePlace: ImageView = itemView.findViewById(R.id.delete_favorite_place)
        var showDetailsOfFavorite: ConstraintLayout =
            itemView.findViewById(R.id.favorite_places_constraintLayout)
    }


}