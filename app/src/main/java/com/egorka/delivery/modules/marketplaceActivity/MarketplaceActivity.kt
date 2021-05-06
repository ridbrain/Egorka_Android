package com.egorka.delivery.modules.marketplaceActivity

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.egorka.delivery.R
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.handlers.BottomSheetHandler
import com.egorka.delivery.services.BottomState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_marketplace.*
import kotlinx.android.synthetic.main.bottom_sheet_pay.*

class MarketplaceActivity : AppCompatActivity(), MarketplaceActivityInterface {

    private var presenter: MarketplacePresenterInterface? = null
    private var bottomSheet: BottomSheetBehavior<LinearLayout>? = null

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

        bottomSheet = BottomSheetBehavior.from(bottomSheetPay)
        bottomSheet?.isHideable = true
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheet?.addBottomSheetCallback(BottomSheetHandler { presenter?.bottomStateChanged(it) })

        presenter = MarketplacePresenter(this)

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

    override fun setPlaceLabel(text: String) {
        dropEditText.setText(text)
    }

}