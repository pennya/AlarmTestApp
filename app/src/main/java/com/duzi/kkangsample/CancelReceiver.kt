package com.duzi.kkangsample

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent

class CancelReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val bIntent = Intent("test.ACTION_ALARM")
        val alarmIntent = PendingIntent.getActivity(context, 100, bIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(alarmIntent)
        alarmIntent.cancel()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(222)
    }
}
