package com.abdelrahman.raafat.climateClue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.abdelrahman.raafat.climateClue.databinding.ActivityMainBinding
import com.abdelrahman.raafat.climateClue.utils.ConstantsValue
import com.abdelrahman.raafat.climateClue.utils.LocaleHelper
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class MainActivity : AppCompatActivity() {
    private val alertID = 0
    private val favoriteID = 1
    private val homeID = 2
    private val timeTableID = 3
    private val settingID = 4

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.updateLocale(this, ConstantsValue.language)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

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
                    findNavController(binding.navHostFragment)
                        .navigate(R.id.home_fragment)
                }
                favoriteID -> {
                    findNavController(binding.navHostFragment)
                        .navigate(R.id.favorite_Fragment)
                }
                alertID -> {
                    findNavController(binding.navHostFragment)
                        .navigate(R.id.alert_Fragment)
                }
                settingID -> {
                    findNavController(binding.navHostFragment)
                        .navigate(R.id.setting_fragment)
                }
                timeTableID -> {
                    findNavController(binding.navHostFragment)
                        .navigate(R.id.timetable_fragment)
                }
            }
        }

        binding.bottomNav.setOnShowListener {

        }

        binding.bottomNav.show(homeID, true)
    }

    fun restartFragment() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        when (findNavController(binding.navHostFragment).currentDestination?.id) {
            R.id.home_fragment -> {
                binding.bottomNav.show(homeID, true)
            }
            R.id.timetable_fragment -> {
                binding.bottomNav.show(timeTableID, true)
            }
            R.id.setting_fragment -> {
                binding.bottomNav.show(settingID, true)
            }
            R.id.alert_Fragment -> {
                binding.bottomNav.show(alertID, true)
            }
            R.id.favorite_Fragment -> {
                binding.bottomNav.show(favoriteID, true)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}