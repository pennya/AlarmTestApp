package com.duzi.kkangsample

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var prefs: SharedPreferences? = null
    var alarm: AlarmManager? = null
    var preIntent: PendingIntent? = null
    var aIntent: Intent? = null
    var alarmIntent: PendingIntent? = null
    var isClick: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 알람 울리기 1분전에 이 Noti를 실행시켜달라는 의뢰
        aIntent = Intent(this, NotiReceiver::class.java)
        preIntent = PendingIntent.getBroadcast(this, 50, aIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 실제 액티비티를 실행시키기위한 의뢰
        val bIntent = Intent("test.ACTION_ALARM")
        alarmIntent = PendingIntent.getActivity(this, 100, bIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val hour = prefs?.getInt("hour", -1)
        val minute = prefs?.getInt("minute", -1)
        val enable = prefs?.getBoolean("enable", false)

        if((hour != null && minute != null) && hour > -1 && minute > -1) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val sd = SimpleDateFormat("HH:mm")
            mission1_time.text = sd.format(calendar.time)
        }

        if(enable != null && enable) {
            mission1_switch.isChecked = true
        }


        mission1_switch.setOnCheckedChangeListener { radioGroup, isChecked ->
            val editor = prefs?.edit()
            editor?.putBoolean("enable", isChecked)
            editor?.apply()

            if(isChecked) {
                if(isClick != null && !(isClick as Boolean)) {
                    val hour = prefs?.getInt("hour", -1)
                    val minute = prefs?.getInt("minute", -1)

                    if((hour != null && minute != null) && hour > -1 && minute > -1) {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)

                        alarm?.set(AlarmManager.RTC, calendar.timeInMillis - 60000, preIntent)
                        alarm?.set(AlarmManager.RTC, calendar.timeInMillis, alarmIntent)
                    }
                }
            } else {
                alarm?.cancel(preIntent)
                alarm?.cancel(alarmIntent)
                preIntent?.cancel()
                alarmIntent?.cancel()
                editor?.putBoolean("enable", false)
            }
        }

        mission1_fab.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                isClick = true

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val editor = prefs?.edit()
                editor?.putInt("hour", hourOfDay)
                editor?.putInt("minute", minute)
                editor?.putBoolean("enable", true)
                editor?.apply()

                val sd = SimpleDateFormat("HH:mm")
                mission1_time.text = sd.format(calendar.time)
                mission1_switch.isChecked = true

                aIntent?.putExtra("time", sd.format(calendar.time))
                alarm?.set(AlarmManager.RTC, calendar.timeInMillis - 120000, preIntent)
                alarm?.set(AlarmManager.RTC, calendar.timeInMillis, alarmIntent)
                isClick = false

            }, currentHour, currentMinute, false).show()

        }
    }
}
