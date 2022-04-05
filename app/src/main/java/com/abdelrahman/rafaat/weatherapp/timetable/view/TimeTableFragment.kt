package com.abdelrahman.rafaat.weatherapp.timetable.view


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
                ConcreteLocaleSource.getInstance(view.context)
            )
        )

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CurrentPlaceViewModel::class.java)
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

    companion object {
        fun newInstance() =
            TimeTableFragment().apply {
                arguments = Bundle().apply {
                  
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }
}