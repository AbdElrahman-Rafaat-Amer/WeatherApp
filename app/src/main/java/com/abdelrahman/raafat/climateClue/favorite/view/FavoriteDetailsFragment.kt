package com.abdelrahman.raafat.climateClue.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.abdelrahman.raafat.climateClue.R
import com.abdelrahman.raafat.climateClue.databinding.FragmentHomeBinding
import com.abdelrahman.raafat.climateClue.favorite.viewmodel.FavoritePlaceViewModel
import com.abdelrahman.raafat.climateClue.model.WeatherResponse
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeAdapter
import com.abdelrahman.raafat.climateClue.ui.home.view.HomeItem
import com.abdelrahman.raafat.climateClue.ui.itemDecorators.SpacingItemDecoration

class FavoriteDetailsFragment : Fragment() {

    private val viewModel: FavoritePlaceViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val homeAdapter = HomeAdapter()

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
        getData()
        observeViewModel()

    }

    private fun initUI() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        layoutManager.spanSizeLookup = generateSpanSizeLookup()
        binding.homeRecyclerView.layoutManager = layoutManager
        binding.homeRecyclerView.adapter = homeAdapter
        // Define space in pixels
        val verticalSpace = resources.getDimensionPixelSize(R.dimen.vertical_space)
        val horizontalSpace = resources.getDimensionPixelSize(R.dimen.horizontal_space)
        binding.homeRecyclerView.addItemDecoration(SpacingItemDecoration(verticalSpace, horizontalSpace, spanCount = 3))
    }

    private fun generateSpanSizeLookup(): GridLayoutManager.SpanSizeLookup =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                when (homeAdapter.getItemViewType(position)) {
                    HomeItem.DayInfoItem::class.java.hashCode() -> 1
                    else -> 3
                }
        }

    private fun getData() {
        val latitude = arguments?.getString("LAT") as String
        val longitude = arguments?.getString("LONG") as String
        viewModel.getDetailsOfSelectedFavorite(latitude, longitude, ConstantsValue.language)
    }

    private fun observeViewModel() {
        viewModel.selectedFavoritePlaces.observe(viewLifecycleOwner) {
            when (it) {
                is WeatherResponse -> {

                }
            }
        }
    }


}