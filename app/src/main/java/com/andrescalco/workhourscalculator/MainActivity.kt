package com.andrescalco.workhourscalculator

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val timeArrived = getSavedArrivedTime()
        showPlusEightHoursText(timeArrived)
        showArrivedText(timeArrived)

        clockIcon.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val currentTime = LocalDateTime.now()

        val timePickerListener = fun(_: TimePicker, hourOfDay: Int, minute: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val newTimeArrived =
                LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())

            showPlusEightHoursText(newTimeArrived)
            showArrivedText(newTimeArrived)
            TimeStoreManager.setArrivedTime(this, newTimeArrived.toString())
        }

        val timePicker = TimePickerDialog(
            this,
            timePickerListener,
            currentTime.hour,
            currentTime.minute,
            true
        )

        timePicker.show()

    }

    private fun showArrivedText(timeArrived: LocalDateTime) {
        val arrivedText = getString(R.string.arrived_text)
        val formattedArrivedTime = formatTime(timeArrived)

        val spannable = SpannableString("$arrivedText $formattedArrivedTime")
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.colorGreen)),
            spannable.length - (formattedArrivedTime.length), spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textArrivedText.text = spannable
    }

    private fun showPlusEightHoursText(timeArrived: LocalDateTime) {
        val plusEightHours = timeArrived.plusMinutes(EIGHT_AND_HALF_HOURS)
        val formattedPlusEightHours = formatTime(plusEightHours)
        val mainText = getString(R.string.main_text)

        val spannable = SpannableString("$mainText $formattedPlusEightHours")
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.colorOrange)),
            spannable.length - (formattedPlusEightHours.length), spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textMainText.text = spannable
    }

    private fun formatTime(time: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern(HOUR_FORMAT)
        return time.format(formatter).toString()
    }

    private fun getSavedArrivedTime(): LocalDateTime {
        val currentTime = LocalDateTime.now()
        val fallbackTime = currentTime.toString();

        val savedTime = TimeStoreManager.getArrivedTime(this, fallbackTime)

        return if (LocalDateTime.parse(savedTime).dayOfMonth != currentTime.dayOfMonth) {
            TimeStoreManager.setArrivedTime(this, fallbackTime)
            currentTime
        } else {
            LocalDateTime.parse(savedTime)
        }
    }
}