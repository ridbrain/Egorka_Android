package com.egorka.delivery.modules.marketplaceActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.adapters.AddressAdapter
import com.egorka.delivery.adapters.TypeData
import com.egorka.delivery.delegates.EditTextWatcher
import com.egorka.delivery.entities.Delivery
import com.egorka.delivery.entities.Dictionary
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.handlers.BottomSheetHandler
import com.egorka.delivery.handlers.SeekBarHandler
import com.egorka.delivery.services.BottomState
import com.egorka.delivery.services.setAccentColorButton
import com.egorka.delivery.services.setPhoneMask
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_marketplace.*
import kotlinx.android.synthetic.main.activity_marketplace.dropEditText
import kotlinx.android.synthetic.main.activity_marketplace.dropFieldButton
import kotlinx.android.synthetic.main.activity_marketplace.pickupEditText
import kotlinx.android.synthetic.main.activity_marketplace.pickupFieldButton
import kotlinx.android.synthetic.main.activity_marketplace.rootView
import kotlinx.android.synthetic.main.activity_marketplace.suggestionsRecycler
import kotlinx.android.synthetic.main.bottom_sheet_pay.*
import java.util.*
import kotlin.collections.ArrayList

class MarketplaceActivity : AppCompatActivity(), MarketplaceActivityInterface {

    private var presenter: MarketplacePresenterInterface? = null
    private var bottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private var addressAdapter: AddressAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        cancelButton.setOnClickListener { onBackPressed() }
        howItWorkButton.setOnClickListener { pressHowItWork() }
        dateFieldButton.setOnClickListener { pressDataInfo() }
        boxFieldButton.setOnClickListener { pressBoxInfo() }
        palletFieldButton.setOnClickListener { pressPalletInfo() }
        dropEditText.setOnClickListener { presenter?.pressPlaces() }
        rootView.setOnClickListener { presenter?.hideKeyboard() }
        pickupEditText.addTextChangedListener(EditTextWatcher { presenter?.textDidChange(it) })
        pickupEditText.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter?.selectTextField() }
        pickupEditText.setOnClickListener { presenter?.selectTextField() }
        pickupFieldButton.setOnClickListener { presenter?.pressAddressButton() }
        dateEditText.setOnClickListener { presenter?.pressDate() }
        dropFieldButton.setOnClickListener { presenter?.openMarketMap() }

        palletSeekBar.setOnSeekBarChangeListener(SeekBarHandler { presenter?.palletCountChange(it) })
        boxSeekBar.setOnSeekBarChangeListener(SeekBarHandler { presenter?.boxCountChange(it) })

        phoneField.setPhoneMask()

        bottomSheet = BottomSheetBehavior.from(bottomSheetPay)
        bottomSheet?.isHideable = true
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheet?.addBottomSheetCallback(BottomSheetHandler { presenter?.bottomStateChanged(it) })

        addressAdapter = AddressAdapter { presenter?.selectAddress(it) }

        suggestionsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        suggestionsRecycler.adapter = addressAdapter

        presenter = MarketplacePresenter(this)

    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("Внимание")
            .setMessage("Маршрут будет удалён. Отменить оформление заказа?")
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ -> super.onBackPressed() }
            .setNegativeButton("Нет") { _, _ -> }

        builder.create().show()

    }

    override fun setBottomSheetState(state: BottomState) {

        when (state) {
            BottomState.Small -> {
                bottomSheet?.peekHeight = resources.getDimension(R.dimen._125sdp).toInt()
                bottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            BottomState.Medium -> {
                bottomSheet?.peekHeight = resources.getDimension(R.dimen._125sdp).toInt()
                bottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            BottomState.Big -> {
                bottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

    }

    override fun getContext(): Activity {
        return this
    }

    private fun pressHowItWork() {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("")
            .setMessage("Доставка до маркетплейсов стала простой и доступной.Больше не нужно звонить в рабочее время нашему менеджеру и оформлять заказ по телефону. Теперь заказ вы можете оформить самостоятельно в любое время суток на все доступные склады, куда мы возим ваши любимые товары. Если вам сложно разобраться - позвоните нам, хотя мы старались сделать сервис как 'раз-два-три'.")
            .setCancelable(false)
            .setPositiveButton("Понятно") { _, _ ->  }

        builder.create().show()

    }

    private fun pressDataInfo() {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("Дни доставок")
            .setMessage("OZON: Вторник, Четверг, Суббота.\nWildBerries: Понедельник — Воскресенье\nЯндекс.Маркет: Понедельник — Воскресенье")
            .setCancelable(false)
            .setPositiveButton("Понятно") { _, _ ->  }

        builder.create().show()

    }

    private fun pressBoxInfo() {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("")
            .setMessage("OZON — 175 руб./коробка\nWildBerries — 125 руб./коробка\nБеру! — 175 руб./коробка")
            .setCancelable(false)
            .setPositiveButton("Понятно") { _, _ ->  }

        builder.create().show()

    }

    private fun pressPalletInfo() {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("")
            .setMessage("OZON — 1900 руб./паллет\nWildBerries — 1400 руб./паллет\nБеру! — 1900 руб./паллет")
            .setCancelable(false)
            .setPositiveButton("Понятно") { _, _ ->  }

        builder.create().show()

    }

    override fun pressPlaces(locations: List<OrderLocation>) {

        val picker = NumberPicker(this)
        val points = ArrayList<String>()
        var index = 0

        locations.forEach { location ->
            location.Point?.Name?.let {
                points.add(it)
            }
        }

        picker.minValue = 0
        picker.maxValue = locations.size - 1
        picker.wrapSelectorWheel = false
        picker.displayedValues = points.toArray(arrayOfNulls<String>(0))

        picker.setOnValueChangedListener { _, _, newVal ->
            index = newVal
        }

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("")
            .setView(picker)
            .setCancelable(false)
            .setPositiveButton("Выбрать") { _, _ ->
                presenter?.selectPlace(locations[index])
            }

        builder.create().show()

    }

    override fun setPickupLabel(text: String) {
        pickupEditText.setText(text)
    }

    override fun setPlaceLabel(text: String) {
        dropEditText.text = text
    }

    override fun setSuggestions(suggestion: List<Dictionary.Suggestion>) {
        addressAdapter?.suggestions = suggestion
        addressAdapter?.notifyDataSetChanged()
    }

    override fun setFocus(size: Int) {
        pickupEditText.requestFocus()
        pickupEditText.setSelection(size)
    }

    override fun deleteFocus() {
        pickupEditText.clearFocus()
    }

    override fun showSuggestions(show: Boolean) {
        suggestionsRecycler.isVisible = show
        marketplaceView.isVisible = !show
    }

    @SuppressLint("SetTextI18n")
    override fun setInfoFields(type: TypeData, price: Delivery.TotalPrice) {

        typeLabel.text = type.label
        typeImage.setImageResource(type.icon!!)
        priceLabel.text = "${price.Total?.div(100)} ₽"

    }

    override fun hideBottomSheet() {
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun showDatePicker(calendar: Calendar) {

        val datePickerDialog = DatePickerDialog(this, R.style.DatePicker, { _, year, month, day ->
            presenter?.setDate(year, month, day)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) )

        datePickerDialog.show()
        datePickerDialog.setAccentColorButton()

    }

    override fun setDate(text: String) {
        dateEditText.text = text
    }

    override fun setBoxLabel(text: String) {
        boxEditText.text = text
    }

    override fun setPalletLabel(text: String) {
        palletEditText.text = text
    }

    override fun changeIconFirstField(edit: Boolean) {
        if (edit) {
            pickupFieldButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_remove))
        } else {
            pickupFieldButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cross_hair))
        }
    }

    override fun addressFieldFocused(): Boolean {
        return pickupEditText.isFocused
    }

    override fun clearAddressField() {
        pickupEditText.setText("")
        pickupEditText.requestFocus()
        pickupEditText.setSelection(0)
    }

}