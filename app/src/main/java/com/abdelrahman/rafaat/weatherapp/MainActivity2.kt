package com.abdelrahman.rafaat.weatherapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {
    private val TAG = "MainActivity2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var repository = Repository.getInstance(
            this,
            WeatherClient.getInstance(),
            ConcreteLocaleSource.getInstance(this)
        )

        CoroutineScope(Dispatchers.Main).launch {
            var alerts = repository.localSource.getWeatherFromDataBase()?.alerts
            Log.i(TAG, "onCreate: size-----> ${alerts?.size}")
            Log.i(TAG, "onCreate: size-----> $alerts")
            for (i in 0 until alerts?.size!!) {
                Log.i(TAG, "onCreate: element-----> " + alerts[i] + "\n\n")
            }

        }


    }
}