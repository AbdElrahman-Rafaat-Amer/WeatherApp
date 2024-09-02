package com.abdelrahman.raafat.climateClue

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.abdelrahman.rafaat.weatherapp.databinding.ActivityMainBinding
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import java.util.*

class MainActivity : AppCompatActivity() {
    private val alertID = R.id.alert_Fragment
    private val favoriteID = R.id.favorite_Fragment
    private val homeID = R.id.home_fragment
    private val timeTableID = R.id.timetable_fragment
    private val settingID = R.id.setting_fragment

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setAppLocale(ConstantsValue.language)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

    }

    private fun initBottomNavigation() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val menuItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_alert, // the icon
                R.drawable.avd_notification, // the AVD that will be shown in FAB
                alertID
            ),
            CbnMenuItem(
                R.drawable.ic_favorite,
                R.drawable.avd_favorite,
                favoriteID
            ),
            CbnMenuItem(
                R.drawable.ic_home,
                R.drawable.avd_home,
                homeID
            ),
            CbnMenuItem(
                R.drawable.ic_timetable,
                R.drawable.avd_timetable,
                timeTableID
            ),
            CbnMenuItem(
                R.drawable.ic_setting,
                R.drawable.avd_settings,
                settingID
            )
        )
        binding.bottomNavigation.setMenuItems(menuItems, 2)
        binding.bottomNavigation.setupWithNavController(navController)
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