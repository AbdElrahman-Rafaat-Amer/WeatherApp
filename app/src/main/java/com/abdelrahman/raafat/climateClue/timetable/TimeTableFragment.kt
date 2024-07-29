package com.abdelrahman.raafat.climateClue.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.raafat.climateClue.databinding.FragmentTimeTableBinding
import com.abdelrahman.raafat.climateClue.homeplaces.viewmodel.CurrentPlaceViewModel
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import com.abdelrahman.raafat.climateClue.utils.ConnectionLiveData

class TimeTableFragment : Fragment() {
    private lateinit var binding: FragmentTimeTableBinding
    private lateinit var dayAdapter: DayAdapter
    private val viewModel: CurrentPlaceViewModel by activityViewModels()

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