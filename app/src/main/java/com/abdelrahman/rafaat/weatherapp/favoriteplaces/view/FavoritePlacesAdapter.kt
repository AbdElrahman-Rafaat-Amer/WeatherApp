package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.FavoritePlaces
import java.util.*

private const val TAG = "FavoritePlacesAdapter"

class FavoritePlacesAdapter(private var fragment: OnDeleteFavorite) :
    RecyclerView.Adapter<FavoritePlacesAdapter.ViewHolder>() {

    private var favorites: List<FavoritePlaces> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritePlacesAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        val favoritePlace = favorites[position]
        holder.nameOfFavoritePlace.text = favoritePlace.selectedPlaces
        holder.dateOfFavoritePlace.text = favoritePlace.selectedDate
        holder.deleteFavoritePlace.setOnClickListener {
            fragment.deleteFromRoom(favoritePlace)
        }

        holder.showDetailsOfFavorite.setOnClickListener {
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
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameOfFavoritePlace: TextView = itemView.findViewById(R.id.name_of_favorite_place)
        var dateOfFavoritePlace: TextView = itemView.findViewById(R.id.date_added_to_favorite)
        var deleteFavoritePlace: ImageView = itemView.findViewById(R.id.delete_favorite_place)
        var showDetailsOfFavorite: ConstraintLayout =
            itemView.findViewById(R.id.favorite_places_constraintLayout)
    }


}