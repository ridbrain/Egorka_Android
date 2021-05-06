package com.egorka.delivery.modules.detailsActivity

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.egorka.delivery.R
import com.egorka.delivery.entities.*
import com.egorka.delivery.entities.Dictionary
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsPresenter(override val view: DetailsActivityInterface): DetailsPresenterInterface {

    private var mainService: MainService? = null
    private var calendar = Calendar.getInstance().gmt3()
    private var defaultDate = Calendar.getInstance().gmt3()

    init { ServiceConnect(view.getContext()) { mainService = it ; onStart() } }

    override fun onStart() {

        if (mainService?.mainModel?.details == null) { return }
        if (mainService?.mainModel?.detailsIndex == null) { return }

        loadDetails(mainService!!.mainModel.details!!, mainService!!.mainModel.detailsIndex!!)

    }

    private fun loadDetails(location: OrderLocation, index: Int) {

        if (location.Type == LocationType.Pickup) {

            view.setLabels("Откуда забрать?", "Контакты отправителя")
            view.setNumView(ContextCompat.getDrawable(view.getContext(), R.drawable.background_round_red)!!, "A${index+1}")

            if (index == 0) {

                if (location.Date == null) {

                    view.setVisibleDateView(1)

                } else {

                    view.setVisibleDateView(2)
                    setDateAndTime(location.Date!!)

                }

            } else {

                view.setVisibleDateView(0)

            }

        } else {

            view.setLabels("Куда отвезти?", "Контакты получателя")
            view.setNumView(ContextCompat.getDrawable(view.getContext(), R.drawable.background_round_blue)!!, "B${index+1}")
            view.setVisibleDateView(0)

        }

        location.Point?.Address?.let { view.setAddress(it) }
        location.Point?.Entrance?.let { view.setEntrance(it.toString()) }
        location.Point?.Floor?.let { view.setFloor(it.toString()) }
        location.Point?.Room?.let { view.setRoom(it.toString()) }
        location.Contact?.Name?.let { view.setName(it) }
        location.Contact?.PhoneMobile?.let { view.setPhone(it) }
        location.Message?.let { view.setMessage(it) }

        if (location.Point?.Code == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                view.getContext().showKeyboard()
                view.setFocus(0)
            }, 500)
        }

    }

    override fun onBackPressed() {
        view.getContext().hideKeyboard()
        view.clearNumView()
        saveDetails()
    }

    override fun setQuickly(quickly: Boolean) {
        if (quickly) {
            view.setVisibleDateView(1)
        } else {
            view.setVisibleDateView(2)
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun setDateAndTime(date: String) {

        SimpleDateFormat().fromISO(date)?.let {

            calendar.time = it

            view.setDate(calendar.dayMonthYear())
            view.setTime(calendar.hoursMinutes())

        }

    }

    override fun pressDate() {
        view.showDatePicker(calendar)
    }

    override fun pressTime() {
        view.showTimePicker(calendar)
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        view.setDate(calendar.dayMonthYear())
    }

    override fun setTime(hours: Int, minutes: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        view.setTime(calendar.hoursMinutes())
    }

    override fun selectTextField() {
        view.showSuggestions(true)
        view.showAddressFieldButton(true)
    }

    override fun textDidChange(text: String) {
        NetworkHandler(view.getContext()).searchAddress(text) { suggestions ->
            view.setSuggestions(suggestions)
        }
    }

    override fun selectAddress(address: Dictionary.Suggestion) {

        view.setAddress(address.Name.toString())
        view.setFocus(address.Name.toString().length)

        if (address.ID != null) {

            mainService?.mainModel?.details?.let {

                it.Point = Point()
                it.Point?.Address = address.Name
                it.Point?.Code = address.ID

                hideKeyboard()
                view.deleteFocus()

            }

        }

    }

    override fun hideKeyboard() {
        view.showAddressFieldButton(false)
        view.showSuggestions(false)
        view.getContext().hideKeyboard()
    }

    override fun pressAddressFieldButton() {
        view.setAddress("")
        view.setFocus(0)
    }

    private fun saveDetails() {

        mainService?.mainModel?.details?.let { location ->

            val contact = Contact()

            view.getPhone()?.let {
                if (it.length == 18) {
                    contact.PhoneMobile = it
                }
            }

            view.getName()?.let {
                if (it.length > 1) {
                    contact.Name = it
                }
            }

            if (contact.PhoneMobile != null || contact.Name != null) {
                location.Contact = contact
            } else {
                location.Contact = null
            }

            location.Point?.Entrance = null
            location.Point?.Floor = null
            location.Point?.Room = null
            location.Message = null

            view.getEntrance()?.let { location.Point?.Entrance = it.toInt() }
            view.getFloor()?.let { location.Point?.Floor = it.toInt() }
            view.getRoom()?.let { location.Point?.Room = it.toInt() }
            view.getMessage()?.let { location.Message = it }

            if (location.RouteOrder == 1 && !view.getSwitchState()) {
                if (defaultDate < calendar) {
                    location.Date = calendar.iso()
                }
            } else {
                mainService?.mainModel?.details?.Date = null
            }

        }

    }

}