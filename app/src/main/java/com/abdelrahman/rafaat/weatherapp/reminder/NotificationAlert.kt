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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.abdelrahman.rafaat.weatherapp.InitializationScreenActivity
import com.abdelrahman.rafaat.weatherapp.MainActivity2
import com.abdelrahman.rafaat.weatherapp.R
import com.abdelrahman.rafaat.weatherapp.alert.view.AlertFragment
import com.abdelrahman.rafaat.weatherapp.homeplaces.view.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NotificationAlert(var mcontext: Context, workerParams: WorkerParameters) :
    Worker(mcontext, workerParams) {

    private var TAG = "NotificationAlert"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun doWork(): Result {
        Handler(Looper.getMainLooper()).post {
            display("Alert KeyWord")
        }
        return Result.success()
    }


    private fun display(keyword: String) {
        Log.i(TAG, "display: start")


        val mBuilder = NotificationCompat.Builder(applicationContext, "notify_001")
        val ii = Intent(applicationContext, MainActivity2::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            ii,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(keyword)
        bigText.setBigContentTitle("Today's Bible Verse")
        bigText.setSummaryText("Text in detail")

        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_alert_notification)
        mBuilder.setContentTitle("Your Title")
        mBuilder.setContentText("Your text")
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