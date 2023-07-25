package com.example.alrammanager

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import com.example.alrammanager.databinding.FragmentAlarmSetBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmSetFragment : Fragment() {

    private lateinit var binding: FragmentAlarmSetBinding
    private lateinit var alarmTime: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmSetBinding.inflate(inflater, container, false)
        alarmTime = Calendar.getInstance()

        binding.btnSetAlarm.setOnClickListener {
            showTimePicker()
        }

        return binding.root
    }

    private fun showTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                alarmTime.set(Calendar.MINUTE, minute)
                updateAlarmTimeUI()
            },
            hour,
            minute,
            false
        ).show()
    }

    private fun updateAlarmTimeUI() {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.tvAlarmTime.text = sdf.format(alarmTime.time)
    }

    fun getAlarmTimeInMillis(): Long {
        return alarmTime.timeInMillis
    }
}
