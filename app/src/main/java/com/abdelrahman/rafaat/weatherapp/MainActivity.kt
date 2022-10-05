package com.abdelrahman.rafaat.weatherapp

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
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

    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        config.setLocale(Locale(localeCode.lowercase(Locale.ROOT)))
        resources.updateConfiguration(config, dm)
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
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