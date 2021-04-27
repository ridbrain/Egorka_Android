package com.egorka.delivery.modules.detailsActivity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.adapters.AddressAdapter
import com.egorka.delivery.delegates.EditTextWatcher
import com.egorka.delivery.entities.Dictionary.Suggestion
import com.egorka.delivery.services.setAccentColorButton
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.suggestionsRecycler
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.*

class DetailsActivity: AppCompatActivity(), DetailsActivityInterface {

    private var presenter: DetailsPresenterInterface? = null
    private var addressAdapter: AddressAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        presenter = DetailsPresenter(this)

        backButton.setOnClickListener { onBackPressed() }

        val formatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(UnderscoreDigitSlotsParser().parseSlots("+7 (___) ___-__-__")))
        formatWatcher.installOn(phoneField)

        needNowCheckBox.setOnClickListener { presenter?.setQuickly(needNowCheckBox.isChecked) }
        dateField.setOnClickListener { presenter?.pressDate() }
        timeField.setOnClickListener { presenter?.pressTime() }

        addressField.addTextChangedListener(EditTextWatcher { presenter?.textDidChange(it) })
        addressField.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter?.selectTextField() }
        addressField.setOnClickListener { presenter?.selectTextField() }
        addressFieldButton.setOnClickListener { presenter?.pressAddressFieldButton() }

        addressAdapter = AddressAdapter { presenter?.selectAddress(it) }

        suggestionsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        suggestionsRecycler.adapter = addressAdapter

        rootView.setOnClickListener { presenter?.hideKeyboard() }

    }

    override fun setNumView(icon: Drawable, text: String) {
        numView.background = icon
        numView.text = text
    }

    override fun setLabels(address: String, contacts: String) {
        addressLabel.text = address
        contactsLabel.text = contacts
    }

    override fun onBackPressed() {
        presenter?.onBackPressed()
        super.onBackPressed()
    }

    override fun setVisibleDateView(state: Int) {
        when (state) {
            0 -> {
                labelTimeView.isVisible = false
                timeView.isVisible = false
            }
            1 -> {
                labelTimeView.isVisible = true
                needNowCheckBox.isChecked = true
                timeView.isVisible = false
            }
            2 -> {
                labelTimeView.isVisible = true
                needNowCheckBox.isChecked = false
                timeView.isVisible = true
            }
        }
    }

    override fun showDatePicker(calendar: Calendar) {

        val datePickerDialog = DatePickerDialog(this, R.style.DatePicker, { _, year, month, day ->
            presenter?.setDate(year, month, day)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) )

        datePickerDialog.show()
        datePickerDialog.setAccentColorButton()

    }

    override fun showTimePicker(calendar: Calendar) {

        val datePickerDialog = TimePickerDialog(this, R.style.DatePicker, { _, hour, minutes ->
            presenter?.setTime(hour, minutes)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

        datePickerDialog.show()
        datePickerDialog.setAccentColorButton()

    }

    override fun showSuggestions(show: Boolean) {
        suggestionsRecycler.isVisible = show
        detailsView.isVisible = !show
    }

    override fun setSuggestions(suggestion: List<Suggestion>) {
        addressAdapter?.suggestions = suggestion
        addressAdapter?.notifyDataSetChanged()
    }

    override fun setFocus(size: Int) {
        addressField.requestFocus()
        addressField.setSelection(size)
    }

    override fun deleteFocus() {
        addressField.clearFocus()
    }

    override fun showAddressFieldButton(show: Boolean) {
        addressFieldButton.isVisible = show
    }

    override fun getContext(): Activity { return this }
    override fun getPhone(): String? { return phoneField.text.toString().ifEmpty { null } }
    override fun getEntrance(): String? { return entranceField.text.toString().ifEmpty { null } }
    override fun getFloor(): String? { return floorField.text.toString().ifEmpty { null } }
    override fun getRoom(): String? { return roomField.text.toString().ifEmpty { null } }
    override fun getMessage(): String? { return messageField.text.toString().ifEmpty { null } }
    override fun getName(): String? { return nameField.text.toString().ifEmpty { null } }
    override fun getSwitchState(): Boolean { return needNowCheckBox.isChecked }
    override fun setAddress(address: String) { addressField.setText(address) }
    override fun setPhone(text: String) { phoneField.setText(text) }
    override fun setName(text: String) { nameField.setText(text) }
    override fun setEntrance(text: String) { entranceField.setText(text) }
    override fun setFloor(text: String) { floorField.setText(text) }
    override fun setRoom(text: String) { roomField.setText(text) }
    override fun setMessage(text: String) { messageField.setText(text) }
    override fun setDate(text: String) { dateField.text = text }
    override fun setTime(text: String) { timeField.text = text }
    override fun clearNumView() { numView.text = "" }

}