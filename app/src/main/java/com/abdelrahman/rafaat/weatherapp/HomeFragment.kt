package com.abdelrahman.rafaat.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var locationNametextView: TextView
    private lateinit var currentDateTextView: TextView
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var dailyRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        locationNametextView = view.findViewById(R.id.location_name_textView)
        currentDateTextView = view.findViewById(R.id.current_date_textView)
        hourlyRecyclerView = view.findViewById(R.id.hourly_recyclerView)
        dailyRecyclerView = view.findViewById(R.id.daily_recyclerView)
        return view
    }


    companion object {

    }
}