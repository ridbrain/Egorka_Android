package com.egorka.delivery.modules.newOrderActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.adapters.LocationAdapter
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.adapters.TypeData
import com.egorka.delivery.entities.Delivery
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.handlers.BottomSheetHandler
import com.egorka.delivery.handlers.SimpleTouchHelper
import com.egorka.delivery.services.BottomState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.bottom_sheet_pay.*

class NewOrderActivity: AppCompatActivity(), NewOrderActivityInterface {

    private var presenter: NewOrderPresenterInterface? = null
    private var bottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private var touchHelperPickup: ItemTouchHelper? = null
    private var touchCallbackPickup: SimpleTouchHelper? = null
    private var touchHelperDrop: ItemTouchHelper? = null
    private var touchCallbackDrop: SimpleTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)

        cancelButton.setOnClickListener { onBackPressed() }

        pickupRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        dropRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        touchCallbackPickup = SimpleTouchHelper(this) { presenter?.deleteLocation(it) }
        touchHelperPickup = ItemTouchHelper(touchCallbackPickup!!)
        touchCallbackDrop = SimpleTouchHelper(this) { presenter?.deleteLocation(it) }
        touchHelperDrop = ItemTouchHelper(touchCallbackDrop!!)

        bottomSheet = BottomSheetBehavior.from(bottomSheetPay)
        bottomSheet?.isHideable = true
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheet?.addBottomSheetCallback(BottomSheetHandler { presenter?.bottomStateChanged(it) })

        addPickupButton.setOnClickListener { presenter?.newPickup() }
        addDropButton.setOnClickListener { presenter?.newDrop() }

        whatField.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter?.hideBottomView() }
        whatField.setOnClickListener { presenter?.hideBottomView() }

        coinField.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter?.hideBottomView() }
        coinField.setOnClickListener { presenter?.hideBottomView() }

        rootView.setOnClickListener { presenter?.hideKeyboard() }

        presenter = NewOrderPresenter(this)

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

    override fun onResume() {
        presenter?.onResume()
        super.onResume()
    }

    override fun getContext(): Activity {
        return this
    }

    override fun updateAdapters(pickups: MutableList<OrderLocation>, drops: MutableList<OrderLocation>, numState: NumState) {

        pickupRecycler.adapter = LocationAdapter(this, numState, pickups) { location, index, view ->
            presenter?.openDetails(location, index, view)
        }

        dropRecycler.adapter = LocationAdapter(this, numState, drops) { location, index, view ->
            presenter?.openDetails(location, index, view)
        }

        if (pickups.size > 1) {
            touchHelperPickup?.attachToRecyclerView(pickupRecycler)
            touchCallbackPickup?.locations = pickups
        } else {
            touchHelperPickup?.attachToRecyclerView(null)
        }

        if (drops.size > 1) {
            touchHelperDrop?.attachToRecyclerView(dropRecycler)
            touchCallbackDrop?.locations = drops
        } else {
            touchHelperDrop?.attachToRecyclerView(null)
        }

    }

    override fun getTransitionName(): String {
        return getString(R.string.number_address_transition)
    }

    @SuppressLint("SetTextI18n")
    override fun setInfoFields(type: TypeData, price: Delivery.TotalPrice) {

        typeLabel.text = type.label
        typeImage.setImageResource(type.icon!!)
        priceLabel.text = "${price.Total?.div(100)} ₽"

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

    override fun hideBottomSheet() {
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

}