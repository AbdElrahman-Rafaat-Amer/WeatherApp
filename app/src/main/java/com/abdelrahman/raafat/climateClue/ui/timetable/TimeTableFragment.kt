package com.abdelrahman.raafat.climateClue.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.FragmentTimeTableBinding
import com.abdelrahman.raafat.climateClue.ui.home.viewmodel.HomeViewModel
import com.abdelrahman.raafat.climateClue.ui.itemDecorators.SpacingItemDecoration
import com.abdelrahman.raafat.climateClue.utils.ConnectionLiveData

class TimeTableFragment : Fragment() {
    private lateinit var binding: FragmentTimeTableBinding
    private val imeTableAdapter = TimeTableAdapter()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel.isTimeTable = true
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
        binding.daysRecyclerView.adapter = imeTableAdapter

        val verticalSpace = resources.getDimensionPixelSize(R.dimen.vertical_space)
        val horizontalSpace = resources.getDimensionPixelSize(R.dimen.horizontal_space)
        binding.daysRecyclerView.addItemDecoration(
            SpacingItemDecoration(
                verticalSpace,
                horizontalSpace,
                spanCount = 0
            )
        )
    }

    private fun checkInternet() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            homeViewModel.onInterconnectionChanged(it)
        }
    }

    private fun observeViewModel() {
        homeViewModel.timeTableList.observe(viewLifecycleOwner) {
            if (it != null) {
                imeTableAdapter.setData(it)
            }
        }
    }

    override fun onDestroyView() {
        homeViewModel.isTimeTable = false
        super.onDestroyView()
    }

}