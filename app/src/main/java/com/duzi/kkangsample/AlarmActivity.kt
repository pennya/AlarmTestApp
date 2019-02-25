package com.duzi.kkangsample

import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.preference.PreferenceManager
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_alarm.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity() {

    var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs?.edit()
        editor?.putBoolean("enable", false)
        editor?.apply()

        val hour = prefs?.getInt("hour", -1)
        val minute = prefs?.getInt("minute", -1)

        if((hour != null && minute != null) && hour > -1 && minute > -1) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val sd = SimpleDateFormat("HH:mm")
            mission1_alarm_time.text = sd.format(calendar.time)
        }

        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notiManager.cancel(222)

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if(!pm.isScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        }

        val alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        player = MediaPlayer.create(applicationContext, alarm)
        player?.start()

        mission1_alarm_stop.setOnClickListener {
            player?.stop()
        }
    }
}
