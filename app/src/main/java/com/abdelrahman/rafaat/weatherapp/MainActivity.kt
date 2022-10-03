package com.abdelrahman.rafaat.weatherapp

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.abdelrahman.rafaat.weatherapp.databinding.ActivityMainBinding
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import java.util.*


class MainActivity : AppCompatActivity() {
    private val alertID = 0
    private val favoriteID = 1
    private val homeID = 2
    private val timeTableID = 3
    private val settingID = 4

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale(ConstantsValue.language)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initBottomNavigation()
        initViewPager()


    }

    private fun initBottomNavigation() {
        binding.bottomNav.add(MeowBottomNavigation.Model(alertID, R.drawable.ic_alert))
        binding.bottomNav.add(MeowBottomNavigation.Model(favoriteID, R.drawable.ic_favorite))
        binding.bottomNav.add(MeowBottomNavigation.Model(homeID, R.drawable.ic_home))
        binding.bottomNav.add(MeowBottomNavigation.Model(timeTableID, R.drawable.ic_timetable))
        binding.bottomNav.add(MeowBottomNavigation.Model(settingID, R.drawable.ic_setting))

        binding.bottomNav.setOnClickMenuListener {
            when (it.id) {
                homeID -> {
                    binding.viewPager.currentItem = homeID
                }
                favoriteID -> {
                    binding.viewPager.currentItem = favoriteID
                }
                alertID -> {
                    binding.viewPager.currentItem = alertID
                }
                settingID -> {
                    binding.viewPager.currentItem = settingID
                }
                timeTableID -> {
                    binding.viewPager.currentItem = timeTableID
                }
            }
        }

        binding.bottomNav.setOnShowListener {

        }
    }

    private fun initViewPager() {
        binding.viewPager.adapter =
            ViewPagerAdapter(this.lifecycle, supportFragmentManager, 5)

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.show(position, true)
                Log.i(TAG, "onPageSelected: position-----------> $position")
            }
        })

        binding.viewPager.setCurrentItem(homeID, false)
        binding.bottomNav.show(homeID, true)
    }


    fun restartFragment() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        config.setLocale(Locale(localeCode.lowercase(Locale.ROOT)))
        resources.updateConfiguration(config, dm)
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
    }
}