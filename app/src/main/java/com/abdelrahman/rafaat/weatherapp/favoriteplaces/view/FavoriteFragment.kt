package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.databinding.FragmentFavoriteBinding
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.maps.view.GoogleMapsActivity
import com.abdelrahman.rafaat.weatherapp.model.*
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.abdelrahman.rafaat.weatherapp.utils.ConnectionLiveData
import com.google.android.material.snackbar.Snackbar

private const val TAG = "FavoriteFragment"

class FavoriteFragment : Fragment(), OnDeleteFavorite {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoritePlacesAdapter
    private lateinit var viewModel: FavoritePlaceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")

        initUI()
        initViewModel()
        observeViewModel()

    }

    private fun initUI() {
        favoriteAdapter = FavoritePlacesAdapter(this)
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = favoriteAdapter

        binding.addFavoriteFloatingActionButton.setOnClickListener {
            ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
                if (it) {
                    val intent = Intent(requireContext(), GoogleMapsActivity::class.java)
                    intent.putExtra("FROM", "FAVORITE")
                    startActivity(intent)
                } else {
                    showSnackBar()
                }
            }
        }
    }

    private fun initViewModel() {
        val viewModelFactory = FavoritePlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(requireContext())
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory)[FavoritePlaceViewModel::class.java]

        viewModel.getStoredFavoritePlaces()
    }

    private fun observeViewModel() {
        viewModel.favoritePlaces.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty() || it.isNotEmpty()) {
                binding.noFavoriteImageView.visibility = GONE
                binding.noFavoriteTextView.visibility = GONE
                favoriteAdapter.setList(it)
            } else {
                favoriteAdapter.setList(emptyList())
                binding.noFavoriteImageView.visibility = VISIBLE
                binding.noFavoriteTextView.visibility = VISIBLE
            }
        }
    }

    private fun showSnackBar() {
        val snackBar = Snackbar.make(
            binding.root,
            getString(R.string.error_network),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)
        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
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
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.favoriteDetails_Fragment)
            } else
                showSnackBar()
        }

    }


}
