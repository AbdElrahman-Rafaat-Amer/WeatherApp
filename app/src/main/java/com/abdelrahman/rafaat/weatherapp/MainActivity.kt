package com.abdelrahman.rafaat.weatherapp

import android.location.Address
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abdelrahman.rafaat.weatherapp.alert.view.AlertFragment
import com.abdelrahman.rafaat.weatherapp.favoriteplaces.view.FavoriteFragment
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeFragment
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import com.abdelrahman.rafaat.weatherapp.setting.SettingFragment
import com.abdelrahman.rafaat.weatherapp.timetable.view.TimeTableFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val ID_ALERT = 1
    private val ID_FAVORITES = 2
    private val ID_HOME = 3
    private val ID_TIMETABLE = 4
    private val ID_SETTING = 5

    private lateinit var meo: MeowBottomNavigation
    private lateinit var address: Address
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        meo = findViewById(R.id.bottom_nav)

        //address = intent.getStringExtra("ADDRESS")!!
        //longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
        //latitude = intent.getDoubleExtra("LATITUDE", 0.0)

        address = ConstantsValue.address
        longitude = ConstantsValue.longitude
        latitude = ConstantsValue.latitude

        Toast.makeText(this, "address" + address, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "longitude" + longitude, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "latitude" + latitude, Toast.LENGTH_SHORT).show()



        CoroutineScope(Dispatchers.IO).launch {

            Log.i(TAG, "getWeatherDataArabic: called>")
            var weatherClient = WeatherClient.getInstance().getWeatherDataArabic()

            Log.i(TAG, "getWeatherDataUnits: called>")
            weatherClient = WeatherClient.getInstance().getWeatherDataEnglish()

        }

        //code for bottom nav bar
        meo.add(Model(1, R.drawable.ic_alert))
        meo.add(Model(2, R.drawable.ic_favorite))
        meo.add(Model(3, R.drawable.ic_home))
        meo.add(Model(4, R.drawable.ic_timetable))
        meo.add(Model(5, R.drawable.ic_setting))

        meo.setOnClickMenuListener {
        }

        meo.setOnShowListener { item ->
            when (item.id) {
                ID_HOME -> {
                    Toast.makeText(this, "ID_HOME", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment()).commit()
                }
                ID_FAVORITES -> {
                    Toast.makeText(this, "ID_FEEDS", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FavoriteFragment()).commit()
                }
                ID_ALERT -> {
                    Toast.makeText(this, "ID_MESSAGES", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AlertFragment()).commit()
                }
                ID_SETTING -> {
                    Toast.makeText(this, "ID_TIMETABLE", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingFragment()).commit()
                }
                ID_TIMETABLE -> {
                    Toast.makeText(this, "ID_INFORMATION", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TimeTableFragment()).commit()
                }
            }
        }
        meo.setOnReselectListener {
            //this method for solving problem of double click on icon
        }
        meo.show(ID_HOME, true)
    }


}