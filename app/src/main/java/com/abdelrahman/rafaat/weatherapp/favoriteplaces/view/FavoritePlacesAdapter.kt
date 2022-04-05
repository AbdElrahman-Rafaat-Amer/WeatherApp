package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Hourly
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.Format
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class FavoritePlacesAdapter(context: Context) :
    RecyclerView.Adapter<FavoritePlacesAdapter.ViewHolder>() {

    private val TAG = "FavoritePlacesAdapter"
    private var context = context
    private var favorites: List<Hourly> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritePlacesAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        holder.nameOfFavoritePlace.text = "name"
        holder.dateOfFavoritePlace.text = "date"
        holder.deleteFavoritePlace.setOnClickListener {
            Toast.makeText(context, "deleteFavoritePlace success", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {

        // return favorites.size
        return 30
    }

    fun setList(favorites: List<Hourly>) {

        Log.i(TAG, "setList: after")
        Log.i(TAG, "setList: hours" + favorites.size)
        Log.i(TAG, "setList: this.hours " + this.favorites.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameOfFavoritePlace: TextView = itemView.findViewById(R.id.name_of_favorite_place)
        var dateOfFavoritePlace: TextView = itemView.findViewById(R.id.date_added_to_favorite)
        var deleteFavoritePlace: ImageView = itemView.findViewById(R.id.delete_favorite_place)
    }


}