package com.abdelrahman.rafaat.weatherapp.timetable.view


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModel
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient


class TimeTableFragment : Fragment() {
    private val TAG = "TimeTableFragment"
    private lateinit var daysRecyclerView: RecyclerView
    private lateinit var dayAdapter: DayAdapter
    private lateinit var viewModelFactory: CurrentPlaceViewModelFactory
    private lateinit var viewModel: CurrentPlaceViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: TimeTableFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        viewModelFactory = CurrentPlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(view.context)
            )
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(CurrentPlaceViewModel::class.java)
        if (isInternetAvailable()) {
            Log.i(TAG, "isInternetAvailable: latitude ---> " + ConstantsValue.latitude)
            Log.i(TAG, "isInternetAvailable: longitude ---> " + ConstantsValue.longitude)
            Log.i(TAG, "isInternetAvailable: language ---> " + ConstantsValue.language)
            viewModel.getWeatherFromNetwork(
                ConstantsValue.latitude,
                ConstantsValue.longitude,
                ConstantsValue.language
            )
        } else {
            Log.i(TAG, "onViewCreated: error in network")
            viewModel.getDataFromRoom()
        }
        viewModel.weatherResponse?.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "onCreateView: observe $it")
            when (it) {
                is WeatherResponse -> {
                    assignDataToView(it)
                }
                else -> Toast.makeText(context, "Return is null $it", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initUI(view: View) {

        daysRecyclerView = view.findViewById(R.id.days_recyclerView)
        dayAdapter = DayAdapter(view.context)
        var linerLayoutManager = LinearLayoutManager(view.context)
        linerLayoutManager.orientation = LinearLayoutManager.VERTICAL
        daysRecyclerView.layoutManager = linerLayoutManager
        daysRecyclerView.adapter = dayAdapter
    }


    private fun assignDataToView(weatherResponse: WeatherResponse) {
        dayAdapter.setList(weatherResponse.daily)
        dayAdapter.notifyDataSetChanged()

    }

    private fun isInternetAvailable(): Boolean {
        var isAvailable = false
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                isAvailable = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        true
                    }
                    else -> false
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        Log.i(TAG, "isInternetAvailable: isAvailable $isAvailable")
        return isAvailable
    }
}