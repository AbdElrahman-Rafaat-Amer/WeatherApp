package com.abdelrahman.raafat.climateClue.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.FragmentHomeBinding
import com.abdelrahman.raafat.climateClue.utils.ConnectionLiveData
import com.abdelrahman.raafat.climateClue.ui.itemDecorators.SpacingItemDecoration
import com.abdelrahman.raafat.climateClue.ui.home.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val homeAdapter = HomeAdapter()
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        checkInternet()
        observeViewModel()

    }

    private fun initUI() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        layoutManager.spanSizeLookup = generateSpanSizeLookup()
        binding.homeRecyclerView.layoutManager = layoutManager
        binding.homeRecyclerView.adapter = homeAdapter
        val verticalSpace = resources.getDimensionPixelSize(R.dimen.vertical_space)
        val horizontalSpace = resources.getDimensionPixelSize(R.dimen.horizontal_space)
        binding.homeRecyclerView.addItemDecoration(SpacingItemDecoration(verticalSpace, horizontalSpace, spanCount = 3))
    }

    private fun generateSpanSizeLookup(): SpanSizeLookup =
        object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                when (homeAdapter.getItemViewType(position)) {
                    HomeItem.DayInfoItem::class.java.hashCode() -> 1
                    else -> 3
                }
        }

    private fun checkInternet() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            viewModel.onInterconnectionChanged(it)
        }
    }

    private fun observeViewModel() {
        viewModel.homeList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.loadingAnimationView.visibility = VISIBLE
                binding.homeRecyclerView.visibility = GONE
            } else {
                binding.loadingAnimationView.visibility = GONE
                binding.homeRecyclerView.visibility = VISIBLE
                homeAdapter.setData(it)
            }
        }
    }

}