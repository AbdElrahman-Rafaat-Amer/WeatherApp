package com.abdelrahman.rafaat.weatherapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "SplashScreenActivity"

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = this.getSharedPreferences("SETTING", Context.MODE_PRIVATE)
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
        Log.d(TAG, "getDefaultValues: language----------->" + ConstantsValue.language)
        if (ConstantsValue.language == "auto")
            ConstantsValue.language = Resources.getSystem().configuration.locales[0].language
        Log.d(TAG, "getDefaultValues: language----------->" + ConstantsValue.language)
        // sharedPreferences.getString("CURRENT_LANGUAGE", Locale.getDefault().language).toString()

        ConstantsValue.tempUnit =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getString("temperature_unit", "K")!!
        Log.d(TAG, "getDefaultValues: tempUnit----------->" + ConstantsValue.tempUnit)
        //sharedPreferences.getString("CURRENT_TEMPERATURE", "k").toString()

        ConstantsValue.windSpeedUnit =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getString("wind_speed", "S")!!
        Log.d(TAG, "getDefaultValues: windSpeedUnit----------->" + ConstantsValue.windSpeedUnit)

        //sharedPreferences.getString("CURRENT_WIND_SPEED", "S").toString()

        ConstantsValue.notificationMethod =
            sharedPreferences.getString("CURRENT_NOTIFICATION", "E").toString()

        ConstantsValue.locationMethod =
            sharedPreferences.getString("CURRENT_LOCATION", " ").toString()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}