package com.abdelrahman.rafaat.weatherapp

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abdelrahman.rafaat.weatherapp.alert.view.AlertFragment
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.view.FavoriteFragment
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeFragment
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.model.Daily
import com.abdelrahman.rafaat.weatherapp.setting.view.SettingFragment
import com.abdelrahman.rafaat.weatherapp.timetable.view.TimeTableFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation.Model
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val ID_ALERT = 1
    private val ID_FAVORITES = 2
    private val ID_HOME = 3
    private val ID_TIMETABLE = 4
    private val ID_SETTING = 5
    private var fragmentShow: Int = 0

    private lateinit var meo: MeowBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate: " + ConstantsValue.language)
        setAppLocale(ConstantsValue.language)
        Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        meo = findViewById(R.id.bottom_nav)



        meo.add(Model(ID_ALERT, R.drawable.ic_alert))
        meo.add(Model(ID_FAVORITES, R.drawable.ic_favorite))
        meo.add(Model(ID_HOME, R.drawable.ic_home))
        meo.add(Model(ID_TIMETABLE, R.drawable.ic_timetable))
        meo.add(Model(ID_SETTING, R.drawable.ic_setting))

        meo.setOnClickMenuListener {
            when (it.id) {
                ID_HOME -> {
                    fragmentShow = ID_HOME
                    replaceFragment(HomeFragment())
                }
                ID_FAVORITES -> {
                    fragmentShow = ID_FAVORITES
                    replaceFragment(FavoriteFragment())
                }
                ID_ALERT -> {
                    fragmentShow = ID_ALERT
                    replaceFragment(AlertFragment())

                }
                ID_SETTING -> {
                    fragmentShow = ID_SETTING
                    replaceFragment(SettingFragment())

                }
                ID_TIMETABLE -> {
                    fragmentShow = ID_TIMETABLE
                    replaceFragment(TimeTableFragment())

                }
            }
        }

        meo.setOnShowListener { item ->
            fragmentShow = item.id
        }

        if (savedInstanceState == null) {
            meo.show(ID_HOME, true)
            addFragment(HomeFragment())
            Log.i(TAG, "savedInstanceState == null")
        } else {
            val fragmentID: Int = savedInstanceState.get("FRAGMENT_ID") as Int
            meo.show(fragmentID, true)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("FRAGMENT_ID", fragmentShow)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun restartFragment(currentFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .detach(currentFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .attach(currentFragment)
            .commit()
    }

    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.getDisplayMetrics()
        val config: Configuration = resources.getConfiguration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(Locale(localeCode.toLowerCase()))
        } else {
            config.locale = Locale(localeCode.toLowerCase())
        }
        resources.updateConfiguration(config, dm)
    }
}