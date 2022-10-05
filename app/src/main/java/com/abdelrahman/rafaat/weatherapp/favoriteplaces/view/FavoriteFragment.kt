package com.abdelrahman.rafaat.weatherapp.favoriteplaces.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
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
import com.abdelrahman.rafaat.weatherapp.databinding.DialogLayoutBinding
import com.abdelrahman.rafaat.weatherapp.databinding.FragmentFavoriteBinding
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.maps.GoogleMapsActivity
import com.abdelrahman.rafaat.weatherapp.model.*
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.abdelrahman.rafaat.weatherapp.utils.ConnectionLiveData
import com.abdelrahman.rafaat.weatherapp.utils.connectInternet
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment(), OnDeleteFavorite {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoritePlacesAdapter
    private lateinit var viewModel: FavoritePlaceViewModel
    private var isInternetExist = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initViewModel()
        checkInternet()
        observeViewModel()

    }

    private fun initUI() {
        favoriteAdapter = FavoritePlacesAdapter(this)
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = favoriteAdapter

        binding.addFavoriteFloatingActionButton.setOnClickListener {
            if (isInternetExist) {
                startActivity(Intent(requireContext(), GoogleMapsActivity::class.java))
            } else {
                showSnackBar()
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

      //  viewModel.getStoredFavoritePlaces()
    }

    private fun checkInternet() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            isInternetExist = it
        }
    }

    private fun observeViewModel() {
        viewModel.favoritePlaces.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty() || it.isNotEmpty()) {
                Log.i("Favorite", "observeViewModel:exist size-----------> ${it.size}")
                binding.noFavoritePlaces.visibility = GONE
                binding.noFavoriteTextView.visibility = GONE
                binding.recyclerViewFavorites.visibility = VISIBLE
                favoriteAdapter.setList(it)
            } else {
                Log.i("Favorite", "observeViewModel:none size------------> ${it.size}")
                favoriteAdapter.setList(emptyList())
                binding.noFavoritePlaces.visibility = VISIBLE
                binding.noFavoriteTextView.visibility = VISIBLE
                binding.recyclerViewFavorites.visibility = GONE
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
        snackBar.setAction(getString(R.string.enable)) {
            connectInternet(requireContext())
        }
        snackBar.show()
    }

    override fun deleteFromRoom(favoritePlaces: FavoritePlaces) {
        showDialog(favoritePlaces)
    }

    override fun showDetails(latitude: String, longitude: String, language: String) {
        if (isInternetExist) {
            val bundle = Bundle()
            bundle.putString("LAT", latitude)
            bundle.putString("LONG", longitude)
            Navigation.findNavController(requireView())
                .navigate(R.id.favoriteDetails_Fragment, bundle)
        } else {
            showSnackBar()
        }
    }

    private fun showDialog(favoritePlaces: FavoritePlaces) {
        val binding = DialogLayoutBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        val displayRectangle = Rect()
        val window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        alertDialog.window!!.setLayout(
            (displayRectangle.width() * 0.82f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.dialogMessageTextView.text = getString(R.string.delete_place)
        binding.removeButton.setOnClickListener {
            viewModel.deleteFromRoom(favoritePlaces)
            alertDialog.dismiss()
            Log.i("Favorite", "showDialog: deleteButton")
        }
        binding.cancelButton.setOnClickListener {
            alertDialog.dismiss()
            Log.i("Favorite", "showDialog: cancelButton")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("Favorite", "onResume: --------------------")
        viewModel.getStoredFavoritePlaces()
    }

}
