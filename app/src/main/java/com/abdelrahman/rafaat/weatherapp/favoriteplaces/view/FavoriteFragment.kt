package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeFragment
import com.abdelrahman.rafaat.weatherapp.maps.view.GoogleMapsActivity
import com.abdelrahman.rafaat.weatherapp.model.*
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment(), OnDeleteFavorite {

    private val TAG = "FavoriteFragment"
    private lateinit var noFavoriteTextView: TextView
    private lateinit var noFavoriteImageView: ImageView
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var addFavoriteButton: FloatingActionButton
    private lateinit var favoriteAdapter: FavoritePlacesAdapter
    private lateinit var viewModelFactory: FavoritePlaceViewModelFactory
    private lateinit var viewModel: FavoritePlaceViewModel
    private lateinit var currentView: View

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
        currentView = view

        noFavoriteTextView = view.findViewById(R.id.no_favorite_textView)
        noFavoriteImageView = view.findViewById(R.id.no_favorite_imageView)

        favoriteRecyclerView = view.findViewById(R.id.recyclerView_favorites)
        addFavoriteButton = view.findViewById(R.id.add_favorite_floatingActionButton)
        favoriteAdapter = FavoritePlacesAdapter(view.context, this)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(view.context)
        favoriteRecyclerView.adapter = favoriteAdapter

        viewModelFactory = FavoritePlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(view.context)
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoritePlaceViewModel::class.java)

        viewModel.getStoredFavoritePlaces()

        viewModel.favoritePlaces.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onViewCreated: favoritePlaces----------> " + it.size)
            if (!it.isNullOrEmpty() || it.isNotEmpty()) {
                Log.i(TAG, "observe: true---> $it")
                noFavoriteImageView.visibility = GONE
                noFavoriteTextView.visibility = GONE
                favoriteAdapter.setList(it)
                favoriteAdapter.notifyDataSetChanged()
            } else {
                Log.i(TAG, "observe: false---> $it")
                favoriteAdapter.setList(emptyList())
                favoriteAdapter.notifyDataSetChanged()
                noFavoriteImageView.visibility = VISIBLE
                noFavoriteTextView.visibility = VISIBLE
            }

        })
        addFavoriteButton.setOnClickListener {
            Log.i(TAG, "addFavoriteButton: ")
            if (isInternetAvailable(requireContext())) {
                val intent = Intent(requireContext(), GoogleMapsActivity::class.java)
                intent.putExtra("FROM", "FAVORITE")
                startActivity(intent)
            } else {
                showSnackBar()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStoredFavoritePlaces()
    }

    override fun deleteFromRoom(favoritePlaces: FavoritePlaces) {
        viewModel.deleteFromRoom(favoritePlaces)
        viewModel.getStoredFavoritePlaces()
    }

    override fun showDetails(latitude: String, longitude: String, language: String) {
        viewModel.getDetailsOfSelectedFavorite(latitude, longitude, language)
        Log.i(TAG, "showDetails: ")
        Log.i(TAG, "showDetails: ")
        if (isInternetAvailable(requireContext())) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoriteDetailsFragment(viewModel))
                .addToBackStack(null).commit()
        } else {
            showSnackBar()
        }

    }

    private fun showSnackBar() {
        var snackBar = Snackbar.make(
            currentView.findViewById(R.id.ConstraintLayout_FavoriteFragment),
            getString(R.string.error_network),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)

        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }

}
