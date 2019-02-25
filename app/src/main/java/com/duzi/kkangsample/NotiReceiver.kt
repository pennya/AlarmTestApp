package com.duzi.kkangsample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat

class NotiReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val aIntent = Intent(context, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(context, 70, aIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bIntent = Intent(context, CancelReceiver::class.java)
        val cancelIntent = PendingIntent.getBroadcast(context, 70, bIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder: NotificationCompat.Builder
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channelDescription = "My Channel One Description"

            // 알림 채널 객체를 만들고
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDescription

            // 알림 매니저를 통해 채널을 생성한다
            manager.createNotificationChannel(channel)

            // 알림을 사용하기 위한 빌더를 하나 만듬
            builder = NotificationCompat.Builder(context, channelId)
        } else {
            builder = NotificationCompat.Builder(context)
        }

        // 알림이 어떻게 생길지를 정의
        builder.setSmallIcon(R.drawable.ic_alarm);
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("예정된 알람")
        builder.setContentText(intent.getStringExtra("time"))
        builder.setContentIntent(pIntent)
        builder.addAction(R.drawable.ic_alarm, "Cancel", cancelIntent)
        builder.setAutoCancel(true)  // 터치하면 자동으로 지워짐
        builder.setOngoing(false) // 진행중 알림

        manager.notify(222, builder.build())

    }
}
