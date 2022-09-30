package com.abdelrahman.rafaat.weatherapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import kotlinx.coroutines.*
import java.util.*


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
            sharedPreferences.getString("CURRENT_LANGUAGE", Locale.getDefault().language).toString()

        ConstantsValue.tempUnit = sharedPreferences.getString("CURRENT_TEMPERATURE", "k").toString()

        ConstantsValue.windSpeedUnit =
            sharedPreferences.getString("CURRENT_WIND_SPEED", "S").toString()

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