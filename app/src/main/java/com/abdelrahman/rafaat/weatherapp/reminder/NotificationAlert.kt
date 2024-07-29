package com.abdelrahman.rafaat.weatherapp.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationAlert(private var context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    override fun doWork(): Result {
        checkAlerts()
        Log.i("AlertFragment", "doWork: ------------")
        return Result.success()
    }

    private fun checkAlerts(): String {

        var keyWord = ""
        val repository = Repository.getInstance(
            context,
            WeatherClient.getInstance(),
            ConcreteLocaleSource.getInstance(context)
        )

        CoroutineScope(Dispatchers.Main).launch {
            val alerts = repository.localSource.getWeatherFromDataBase()?.alerts
            if (alerts?.size != null) {
                val alertTag = alerts[0].tags[0]
                if (alertTag == "Rain") {
                    keyWord = context.getString(R.string.rain_alert)
                } else {
                    if (alertTag == "Wind") {
                        keyWord = context.getString(R.string.wind_alert)
                    } else {
                        if (alertTag == "Avalanches") {
                            keyWord = context.getString(R.string.avalanches_alert)

                        } else {
                            if (alertTag == "Fire warning") {
                                keyWord = context.getString(R.string.forest_fire_alert)
                            } else {
                                keyWord = context.getString(R.string.other_alert)
                                keyWord = "Other"
                            }
                        }
                    }
                }
            } else {
                keyWord = context.getString(R.string.no_alert)
            }

            display(keyWord)
        }
        return keyWord
    }


    private fun display(keyword: String) {
        val mBuilder = NotificationCompat.Builder(applicationContext, "notify_001")
        val intent = Intent(applicationContext, context::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_alert_notification)
        mBuilder.setContentTitle(context.getString(R.string.alert))
        mBuilder.setContentText(keyword)

        val mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "Your_channel_id"
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_HIGH
        )
        mNotificationManager.createNotificationChannel(channel)
        mBuilder.setChannelId(channelId)
        mNotificationManager.notify(0, mBuilder.build())
    }


}