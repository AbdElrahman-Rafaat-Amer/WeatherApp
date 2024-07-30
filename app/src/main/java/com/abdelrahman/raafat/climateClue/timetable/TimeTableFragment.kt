package com.abdelrahman.raafat.climateClue.timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.FragmentTimeTableBinding
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.ConnectionLiveData
import com.abdelrahman.raafat.climateClue.homeplaces.viewmodel.HomeViewModel
import com.abdelrahman.raafat.climateClue.utils.SpacingItemDecoration

class TimeTableFragment : Fragment() {
    private lateinit var binding: FragmentTimeTableBinding
    private val dayAdapter = DayAdapter()
    private val viewModel: HomeViewModel by activityViewModels()

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
        val linerLayoutManager = LinearLayoutManager(requireContext())
        binding.daysRecyclerView.layoutManager = linerLayoutManager
        binding.daysRecyclerView.adapter = dayAdapter

        val space = resources.getDimensionPixelSize(R.dimen.horizontal_space)
        binding.daysRecyclerView.addItemDecoration(SpacingItemDecoration(space, space, spanCount = 0))
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
            if (it != null) {
                dayAdapter.setList(it.daily)
            }
        }
    }
}