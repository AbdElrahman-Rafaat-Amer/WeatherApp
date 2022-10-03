package com.abdelrahman.rafaat.weatherapp

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue
import kotlinx.coroutines.*

const val TAG = "SplashScreenActivity"

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        getDefaultValues()


        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            startActivity(
                Intent(
                    this@SplashScreenActivity,
                    InitializationScreenActivity::class.java
                )
            )
            finish()
        }


    }

    private fun getDefaultValues() {
        ConstantsValue.language =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getString("language_name", "auto")!!
        if (ConstantsValue.language == "auto")
            ConstantsValue.language = Resources.getSystem().configuration.locales[0].language

        ConstantsValue.tempUnit =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getString("temperature_unit", "kelvin")!!

        ConstantsValue.windSpeedUnit =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getString("wind_speed", "M/S")!!

        ConstantsValue.locationMethod =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getString("location_method", "GPS")!!

        ConstantsValue.notificationMethod =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getBoolean("toggle_notification", true)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}