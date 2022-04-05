package com.abdelrahman.rafaat.weatherapp.alert.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModel
import com.abdelrahman.rafaat.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource

import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.viewmodel.FavoritePlaceViewModelFactory

import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AlertFragment : Fragment() {

    private val TAG = "AlertFragment"
    private lateinit var noAlertTextView: TextView
    private lateinit var noAlertImageView: ImageView
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var addAlertButton: FloatingActionButton
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var viewModelFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noAlertTextView = view.findViewById(R.id.no_alerts_textView)
        noAlertImageView = view.findViewById(R.id.no_alerts_imageView)

        alertRecyclerView = view.findViewById(R.id.recyclerView_alerts)
        addAlertButton = view.findViewById(R.id.add_alerts_floatingActionButton)
        alertAdapter = AlertAdapter(view.context)
        alertRecyclerView.layoutManager = LinearLayoutManager(view.context)
        alertRecyclerView.adapter = alertAdapter

        viewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource.getInstance(view.context)
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AlertViewModel::class.java)

        addAlertButton.setOnClickListener {
            Log.i(TAG, "addAlertButton: ")
            Toast.makeText(context, "Show dialog to take time and date ", Toast.LENGTH_SHORT).show()
        }
    }

}