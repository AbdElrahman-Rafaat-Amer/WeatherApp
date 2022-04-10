package com.abdelrahman.rafaat.weatherapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.abdelrahman.rafaat.weatherapp.model.ConstantsValue
import kotlinx.coroutines.*
import java.util.*


class SplashScreenActivity : AppCompatActivity() {

    private val TAG = "SplashScreenActivity"
    lateinit var sharedPreferences: SharedPreferences;
    override fun onCreate(savedInstanceState: Bundle?) {
        //Locale.setDefault(Locale.forLanguageTag(ConstantsValue.language))
        super.onCreate(savedInstanceState)

        sharedPreferences = this.getSharedPreferences("SETTING", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_splash_screen)
        printInLogcat()
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

        Log.i(TAG, "getDefaultValues:   ConstantsValue.language  " + ConstantsValue.language)
        Log.i(
            TAG,
            "getDefaultValues: Locale.getDefault().toLanguageTag---> " + Locale.getDefault()
                .toLanguageTag()
        )
        Log.i(TAG, "getDefaultValues: " + ConstantsValue.language)
        ConstantsValue.tempUnit = sharedPreferences.getString("CURRENT_TEMPERATURE", "k").toString()

        ConstantsValue.windSpeedUnit =
            sharedPreferences.getString("CURRENT_WIND_SPEED", "S").toString()

        ConstantsValue.notificationMethod =
            sharedPreferences.getString("CURRENT_NOTIFICATION", "E").toString()

        ConstantsValue.locationMethod =
            sharedPreferences.getString("CURRENT_LOCATION", " ").toString()
    }

    private fun printInLogcat() {
        Log.i(
            TAG,
            "printInLogcat:toLanguageTag------------- " + Locale.getDefault().toLanguageTag()
        )
        Log.i(
            TAG,
            "printInLogcat:displayLanguage------------- " + Locale.getDefault().displayLanguage
        )
        Log.i(
            TAG,
            "printInLogcat:displayCountry ------------- " + Locale.getDefault().displayCountry
        )
        Log.i(TAG, "printInLogcat:displayName ------------- " + Locale.getDefault().displayName)
        Log.i(TAG, "printInLogcat: language------------- " + Locale.getDefault().language)
        Log.i(
            TAG, "printInLogcat : language: " + sharedPreferences.getString(
                "CURRENT_LANGUAGE", Locale.getDefault().country
            )
        )
        Log.i(
            TAG,
            "printInLogcat : temp: " + sharedPreferences.getString("CURRENT_TEMPERATURE", "k")
        )
        Log.i(
            TAG,
            "printInLogcat : wind: " + sharedPreferences.getString("CURRENT_WIND_SPEED", "S")
        )
        Log.i(
            TAG,
            "printInLogcat : location: " + sharedPreferences.getString("CURRENT_LOCATION", " ")
        )
        Log.i(
            TAG,
            "printInLogcat : notification: " + sharedPreferences.getString(
                "CURRENT_NOTIFICATION",
                "E"
            )
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}