package com.abdelrahman.rafaat.weatherapp

import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.abdelrahman.rafaat.weatherapp.databinding.ActivitySplashBinding
import com.abdelrahman.rafaat.weatherapp.utils.ConstantsValue

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDefaultValues()
        binding.splashAnimation.addAnimatorListener(object : AnimatorListener{
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                startActivity(Intent(this@SplashActivity, InitializationScreenActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}

            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })

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
        
        ConstantsValue.notificationMethod =
            PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                .getBoolean("toggle_notification", true)
    }
}