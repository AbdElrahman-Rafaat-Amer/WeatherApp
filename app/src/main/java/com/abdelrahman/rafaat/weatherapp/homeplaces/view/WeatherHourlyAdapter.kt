package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.model.Hourly
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse

import java.util.*

class WeatherHourlyAdapter(context: Context) :
    RecyclerView.Adapter<WeatherHourlyAdapter.ViewHolder?>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHourlyAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

        return 0
    }

    fun setList(movies: List<Hourly>) {

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* var movieYear: TextView
         var movieTitle: TextView
         var movieRate: RatingBar
         var moviePoster: ImageView
         var addToFavorite: Button

         init {
             moviePoster = itemView.findViewById(R.id.movie_imageView)
             movieRate = itemView.findViewById(R.id.movie_ratingBar)
             movieYear = itemView.findViewById(R.id.movie_year_textview)
             movieTitle = itemView.findViewById(R.id.movie_title_textview)
             addToFavorite = itemView.findViewById(R.id.add_to_favoirte_button)
         }*/
    }

}