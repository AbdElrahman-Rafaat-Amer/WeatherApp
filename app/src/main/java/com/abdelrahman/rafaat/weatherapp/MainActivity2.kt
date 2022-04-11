package com.abdelrahman.rafaat.weatherapp


import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
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
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        var repository = Repository.getInstance(
            this,
            WeatherClient.getInstance(),
            ConcreteLocaleSource.getInstance(this)
        )

        CoroutineScope(Dispatchers.Main).launch {
            var alerts = repository.localSource.getWeatherFromDataBase()?.alerts
            Log.i(TAG, "onCreate: size-----> ${alerts?.size}")
            Log.i(TAG, "onCreate: size-----> $alerts")
            if (alerts?.size != null){
                for (i in 0 until alerts?.size!!) {
                    Log.i(TAG, "onCreate: element-----> " + alerts[i].tags.size + "\n\n")

                    if (alerts[i].tags.size > 0){
                        Log.i(TAG, "onCreate: tags-----> " + alerts[i].tags + "\n\n")
                        Log.i(TAG, "onCreate: tags elment-----> " + alerts[i].tags[0] + "\n\n")
                    }else{
                        Log.i(TAG, "onCreate: event-----> " + alerts[i].event + "\n\n")
                    }

                }
            }


        }


    }
}