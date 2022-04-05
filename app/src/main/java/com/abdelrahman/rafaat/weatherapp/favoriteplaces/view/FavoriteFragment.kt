package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.maps.view.MapsActivity
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FavoriteFragment : Fragment() {

    private val TAG = "FavoriteFragment"
    private lateinit var noFavoriteTextView: TextView
    private lateinit var noFavoriteImageView: ImageView
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var addFavoriteButton: FloatingActionButton
    private lateinit var favoriteAdapter: FavoritePlacesAdapter
    private lateinit var viewModelFactory: FavoritePlaceViewModelFactory
    private lateinit var viewModel: FavoritePlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")

        noFavoriteTextView = view.findViewById(R.id.no_favorite_textView)
        noFavoriteImageView = view.findViewById(R.id.no_favorite_imageView)

        favoriteRecyclerView = view.findViewById(R.id.recyclerView_favorites)
        addFavoriteButton = view.findViewById(R.id.add_favorite_floatingActionButton)
        favoriteAdapter = FavoritePlacesAdapter(view.context)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(view.context)
        favoriteRecyclerView.adapter = favoriteAdapter

        viewModelFactory = FavoritePlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource.getInstance(view.context)
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoritePlaceViewModel::class.java)

        addFavoriteButton.setOnClickListener {
            Log.i(TAG, "addFavoriteButton: ")
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }
    }

}
