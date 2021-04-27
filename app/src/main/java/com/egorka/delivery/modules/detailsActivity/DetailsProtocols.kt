package com.egorka.delivery.modules.detailsActivity

import android.app.Activity
import android.graphics.drawable.Drawable
import com.egorka.delivery.entities.Dictionary.Suggestion
import java.util.*

interface DetailsActivityInterface {

    fun getContext(): Activity
    fun setAddress(address: String)
    fun setNumView(icon: Drawable, text: String)
    fun setLabels(address: String, contacts: String)
    fun clearNumView()
    fun setVisibleDateView(state: Int)
    fun setPhone(text: String)
    fun setName(text: String)
    fun setEntrance(text: String)
    fun setFloor(text: String)
    fun setRoom(text: String)
    fun setMessage(text: String)
    fun setDate(text: String)
    fun setTime(text: String)
    fun getName(): String?
    fun getPhone(): String?
    fun getEntrance(): String?
    fun getFloor(): String?
    fun getRoom(): String?
    fun getMessage(): String?
    fun getSwitchState(): Boolean
    fun showDatePicker(calendar: Calendar)
    fun showTimePicker(calendar: Calendar)
    fun showSuggestions(show: Boolean)
    fun setSuggestions(suggestion: List<Suggestion>)
    fun setFocus(size: Int)
    fun deleteFocus()
    fun showAddressFieldButton(show: Boolean)
}

interface DetailsPresenterInterface {

    val view: DetailsActivityInterface

    fun onStart()
    fun onBackPressed()
    fun setQuickly(quickly: Boolean)
    fun pressDate()
    fun pressTime()
    fun setDate(year: Int, month: Int, day: Int)
    fun setTime(hours: Int, minutes: Int)
    fun selectTextField()
    fun textDidChange(text: String)
    fun selectAddress(address: Suggestion)
    fun hideKeyboard()
    fun pressAddressFieldButton()

}