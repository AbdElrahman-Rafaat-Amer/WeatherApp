package com.abdelrahman.rafaat.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var backGroundImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
      //  backGroundImageView = findViewById(R.id.background_imageView)
        CoroutineScope(Dispatchers.Main).launch {
        //    backGroundImageView.setImageResource(R.drawable.splash_screen_clear)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}