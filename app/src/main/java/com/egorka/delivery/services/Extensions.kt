package com.egorka.delivery.services

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.egorka.delivery.R
import java.text.SimpleDateFormat
import java.util.*

enum class BottomState {
    Small,
    Medium,
    Big
}

fun Activity.showKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    window.decorView.rootView?.let { view -> inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0) }
}

fun DatePickerDialog.setAccentColorButton() {
    getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
}

fun TimePickerDialog.setAccentColorButton() {
    getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
}

fun Calendar.gmt3(): Calendar {
    this.timeZone = TimeZone.getTimeZone("GMT+3")
    return this
}

fun SimpleDateFormat.setup(pattern: String, timeZone: TimeZone): SimpleDateFormat {
    this.applyPattern(pattern)
    this.timeZone = timeZone
    return this
}

fun SimpleDateFormat.fromISO(date: String): Date? {
    this.applyPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    return this.parse(date)
}

@SuppressLint("SimpleDateFormat")
fun Calendar.dayMonthYear(): String {
    return SimpleDateFormat().setup("dd MMMM yyyy", this.timeZone).format(this.time)
}

@SuppressLint("SimpleDateFormat")
fun Calendar.hoursMinutes(): String {
    return SimpleDateFormat().setup("HH:mm", this.timeZone).format(this.time)
}

@SuppressLint("SimpleDateFormat")
fun Calendar.iso(): String {
    return SimpleDateFormat().setup("yyyy-MM-dd'T'HH:mm:ssZ", this.timeZone).format(this.time)
}