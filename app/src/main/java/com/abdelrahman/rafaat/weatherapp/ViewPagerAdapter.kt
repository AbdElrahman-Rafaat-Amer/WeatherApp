package com.abdelrahman.rafaat.weatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.abdelrahman.rafaat.weatherapp.alert.view.AlertFragment
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.view.FavoriteFragment
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeFragment
import com.abdelrahman.rafaat.weatherapp.setting.SettingFragment
import com.abdelrahman.rafaat.weatherapp.timetable.TimeTableFragment


class ViewPagerAdapter(
    lifecycle: Lifecycle,
    fragmentManager: FragmentManager,
    private var tabsNumber: Int
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = tabsNumber

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlertFragment()
            1 -> FavoriteFragment()
            2 -> HomeFragment()
            3 -> TimeTableFragment()
            4 -> SettingFragment()
            else -> HomeFragment()
        }
    }

}