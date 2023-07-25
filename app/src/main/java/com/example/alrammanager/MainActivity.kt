package com.example.alrammanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import com.example.alrammanager.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val alarmManager: AlarmManager by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createNotificationChannel()

        val pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // 알람 등록 버튼 클릭 시
        binding.btnSetAlarm.setOnClickListener {
            val alarmSetFragment = pagerAdapter.getItem(0) as AlarmSetFragment
            val alarmTimeInMillis = alarmSetFragment.getAlarmTimeInMillis()
            setAlarm(alarmTimeInMillis)
        }

        // 알람 울릴 때 수행할 작업을 정의하는 BroadcastReceiver 등록
        val intentFilter = IntentFilter(ALARM_ACTION)
        registerReceiver(alarmReceiver, intentFilter)
    }

    private fun setAlarm(alarmTimeInMillis: Long) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeInMillis,
            pendingIntent
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(alarmReceiver)
    }

    inner class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
        FragmentStatePagerAdapter(fm, lifecycle) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AlarmSetFragment()
                else -> AlarmListFragment()
            }
        }

        override fun getCount(): Int {
            return 2 // 탭의 개수
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "알람 설정"
                else -> "알람 목록"
            }
        }
    }


    private fun showAlarmNotification() {
        val channelId = "alarm_channel"

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("알람이 울렸습니다!")
            .setContentText("알람이 설정된 시간입니다.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        const val ALARM_ACTION = "com.example.alrammanager.ALARM_ACTION"
    }

    private val alarmReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 알람 울릴 때 수행할 작업을 처리하세요.
            // 예를 들어, 알람을 울렸을 때 알림을 보여주는 등의 작업을 수행합니다.
            showAlarmNotification()
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "alarm_channel"
                val channelName = "Alarm Channel"
                val channelDescription = "Channel for alarm notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
