package com.abdelrahman.rafaat.weatherapp.reminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import androidx.work.Worker
import androidx.work.WorkerParameters
import com.abdelrahman.rafaat.weatherapp.MainActivity2
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.database.ConcreteLocaleSource
import com.abdelrahman.rafaat.weatherapp.model.Repository
import com.abdelrahman.rafaat.weatherapp.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationAlert(var mcontext: Context, workerParams: WorkerParameters) :
    Worker(mcontext, workerParams) {

    private var TAG = "NotificationAlert"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun doWork(): Result {

        Handler(Looper.getMainLooper()).post {

            display(checkAlerts())
        }
        return Result.success()
    }

    private fun checkAlerts(): String {
        var keyWord: String = ""
        var repository = Repository.getInstance(
            mcontext,
            WeatherClient.getInstance(),
            ConcreteLocaleSource.getInstance(mcontext)
        )

        CoroutineScope(Dispatchers.Main).launch {
            var alerts = repository.localSource.getWeatherFromDataBase()?.alerts
            Log.i(TAG, "onCreate: size-----> ${alerts?.size}")
            Log.i(TAG, "onCreate: size-----> $alerts")
            if (alerts?.size != null) {

                var alertTag = alerts[0].tags[0]
                if (alertTag.equals("Rain")) {
                    keyWord = "Rain"
                } else {
                    if (alertTag.equals("Wind")) {

                        keyWord = "Wind"
                    } else {
                        if (alertTag.equals("Avalanches")) {

                            keyWord = "Avalanches"
                        } else {
                            if (alertTag.equals("Fire warning")) {
                                keyWord = "Fire"
                            } else {
                                keyWord = mcontext.getString(R.string.other_alert)
                                keyWord = "Ohter"
                            }
                        }
                    }
                }
            } else {
                keyWord = "No Alert"
            }


        }
        return keyWord
    }


    private fun display(keyword: String) {
        Log.i(TAG, "display: start")
        val mBuilder = NotificationCompat.Builder(applicationContext, "notify_001")
        val intent = Intent(applicationContext, mcontext::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(keyword)
        bigText.setBigContentTitle(mcontext.getString(R.string.app_name))
        bigText.setSummaryText(keyword)


        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_alert_notification)
        mBuilder.setContentTitle("Alert")
        mBuilder.setContentText(keyword)
        mBuilder.priority = Notification.PRIORITY_MAX
        mBuilder.setStyle(bigText)

        val mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "display: if")
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        } else {

            Log.i(TAG, "display: else")
        }

        mNotificationManager.notify(0, mBuilder.build())
        Log.i(TAG, "display: end")
    }


}