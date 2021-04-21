package com.egorka.delivery.services

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipboardManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

fun Activity.copyString(text: String) {
    val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    clipBoard.setPrimaryClip(ClipData.newPlainText("text", text))
}

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Activity.transparentStatusAndNavigation(systemUiScrim: Int = Color.parseColor("#40000000")) {

    var systemUiVisibility = 0
    var statusBarColor = systemUiScrim
    val winParams = window.attributes

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        statusBarColor = Color.TRANSPARENT
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }

    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    window.decorView.systemUiVisibility = systemUiVisibility

    winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        winParams.flags = winParams.flags and  (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS).inv()
        window.statusBarColor = statusBarColor
    }

    window.attributes = winParams

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