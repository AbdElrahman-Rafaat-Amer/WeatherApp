package com.abdelrahman.rafaat.weatherapp.homeplaces.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.view.FavoriteFragment
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModel
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.model.Daily
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient

class HomeFragment : Fragment(), OnDayClickListener {

    private lateinit var locationNametextView: TextView
    private lateinit var currentDateTextView: TextView
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var weatherHourlyAdapter: WeatherHourlyAdapter
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var weatherDailyAdapter: WeatherDailyAdapter
    private lateinit var factory: CurrentPlaceViewModelFactory
    private lateinit var viewModel: CurrentPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initUI(view)
        factory = CurrentPlaceViewModelFactory(
            Repository.getInstance(
                view.context,
                WeatherClient.getInstance(),
                ConcreteLocaleSource.getInstance(view.context)
            )!!
        )

        viewModel = ViewModelProvider(this).get(CurrentPlaceViewModel::class.java)

        viewModel.weatherResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                weatherDailyAdapter.setList(it.daily)
                weatherDailyAdapter.notifyDataSetChanged()
                weatherHourlyAdapter.setList(it.hourly)
                weatherDailyAdapter.notifyDataSetChanged()
            } else {

                Toast.makeText(context, "Return is null " + it, Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    private fun initUI(view: View) {
        locationNametextView = view.findViewById(R.id.location_name_textView)
        currentDateTextView = view.findViewById(R.id.current_date_textView)
        hourlyRecyclerView = view.findViewById(R.id.hourly_recyclerView)
        weatherHourlyAdapter = WeatherHourlyAdapter(view.context)
        val hourlyManager = LinearLayoutManager(view.context)
        hourlyManager.orientation = LinearLayoutManager.VERTICAL
        hourlyRecyclerView.layoutManager = hourlyManager
        hourlyRecyclerView.adapter = weatherHourlyAdapter


        dailyRecyclerView = view.findViewById(R.id.daily_recyclerView)
        weatherDailyAdapter = WeatherDailyAdapter(view.context)
        val dailyManager = LinearLayoutManager(view.context)
        dailyManager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyRecyclerView.layoutManager = dailyManager
        hourlyRecyclerView.adapter = weatherHourlyAdapter
    }

    override fun showDayDetails(dayStatus: Daily) {
        Toast.makeText(context, "showDayDetails clicked", Toast.LENGTH_SHORT).show()
    }

}