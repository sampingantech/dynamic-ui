package com.sampingan.agentapp.dynamic_ui.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateUtils
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.COLON_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DASH_TEXT
import java.util.*

class DateTimePickerDialog(private val context: Context) {

    private var dateUtils = DateUtils()
    private val calenderToday = Calendar.getInstance()

    fun setValueOfDate(calender: Calendar, returnValue: VoidCallback) {
        val dateOnClicked = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            fun setDate() {
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateSelected =
                    dayOfMonth.toString() + DASH_TEXT + (monthOfYear + 1).toString() + DASH_TEXT + year.toString()
                returnValue.invoke(dateSelected)
            }
            setDate()
        }
        val datePickerDialog = DatePickerDialog(
            context, dateOnClicked,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH))
//        datePickerDialog.datePicker.maxDate = calenderToday.timeInMillis
        datePickerDialog.show()
    }

    fun setValueOfTime(
        calendarSchedule: Calendar,
        successCallback: VoidCallback
    ) {
        val timeSet = Calendar.getInstance()
        val timeOnClicked = TimePickerDialog.OnTimeSetListener {
                _,
                hourOfDay,
                minuteOfDay
            ->

            calendarSchedule.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarSchedule.set(Calendar.MINUTE, minuteOfDay)
            val time = "$hourOfDay$COLON_TEXT$minuteOfDay"
            successCallback.invoke(time)

        }

        timeSet.set(Calendar.HOUR_OF_DAY, calendarSchedule.get(Calendar.HOUR_OF_DAY))
        timeSet.set(Calendar.MINUTE, calendarSchedule.get(Calendar.MINUTE))
        val timePickerDialog = TimePickerDialog(
            context,
            timeOnClicked,
            timeSet.get(Calendar.HOUR_OF_DAY),
            timeSet.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}

typealias VoidCallback = (Any) -> (Unit)