package com.example.alrammanager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 알람 울릴 때 수행할 작업을 처리하세요.
        // 예를 들어, 알림을 보여주는 등의 작업을 수행합니다.
        showAlarmNotification(context)
    }

    private fun showAlarmNotification(context: Context) {
        val channelId = "alarm_channel"

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("알람이 울렸습니다!")
            .setContentText("알람이 설정된 시간입니다.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }
}