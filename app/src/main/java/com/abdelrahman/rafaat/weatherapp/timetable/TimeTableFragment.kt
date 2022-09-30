package com.abdelrahman.rafaat.weatherapp.timetable


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.databinding.FragmentTimeTableBinding
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModel
import com.abdelrahman.rafaat.weatherapp.homeplaces.viewmodel.CurrentPlaceViewModelFactory
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.model.WeatherResponse
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.abdelrahman.rafaat.weatherapp.utils.ConnectionLiveData


class TimeTableFragment : Fragment() {

    private lateinit var binding: FragmentTimeTableBinding
    private lateinit var dayAdapter: DayAdapter
    private lateinit var viewModel: CurrentPlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeTableBinding.inflate(layoutInflater)
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
        dayAdapter = DayAdapter()
        val linerLayoutManager = LinearLayoutManager(requireContext())
        linerLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.daysRecyclerView.layoutManager = linerLayoutManager
        binding.daysRecyclerView.adapter = dayAdapter
    }

    private fun initViewModel() {
        val viewModelFactory = CurrentPlaceViewModelFactory(
            Repository.getInstance(
                requireContext(),
                WeatherClient.getInstance(),
                ConcreteLocaleSource(requireContext())
            )
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[CurrentPlaceViewModel::class.java]
    }

    private fun checkInternet() {
        ConnectionLiveData.getInstance(requireContext())
            .observe(viewLifecycleOwner) {
                if (it) {
                    viewModel.getWeatherFromNetwork(
                        ConstantsValue.latitude,
                        ConstantsValue.longitude,
                        ConstantsValue.language
                    )
                } else
                    viewModel.getDataFromRoom()
            }
    }

    private fun observeViewModel() {
        viewModel.weatherResponse.observe(viewLifecycleOwner) {
            when (it) {
                is WeatherResponse -> {
                    dayAdapter.setList(it.daily)
                }
                else -> Toast.makeText(context, "Return is null $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

}