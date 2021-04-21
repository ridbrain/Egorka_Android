package com.egorka.delivery.modules.newOrderActivity

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.adapters.LocationAdapter
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.entities.NewOrderLocation
import com.egorka.delivery.handlers.BottomSheetHandler
import com.egorka.delivery.handlers.SwipeToDeleteHandler
import com.egorka.delivery.services.BottomState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.bottom_sheet_pay.*

class NewOrderActivity: AppCompatActivity(), NewOrderActivityInterface {

    private var presenter: NewOrderPresenterInterface? = null
    private var bottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private var touchHelperPickup: SwipeToDeleteHandler? = null
    private var touchHelperDrop: SwipeToDeleteHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)

        presenter = NewOrderPresenter(this)

        cancelButton.setOnClickListener { onBackPressed() }

        pickupRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        dropRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        touchHelperPickup = SwipeToDeleteHandler(this) { presenter?.removePickup(it) }
        touchHelperDrop = SwipeToDeleteHandler(this) { presenter?.removeDrop(it) }

        bottomSheet = BottomSheetBehavior.from(bottomSheetPay)
        bottomSheet?.isHideable = true
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheet?.addBottomSheetCallback(BottomSheetHandler { presenter?.bottomStateChanged(it) })

        addPickupButton.setOnClickListener { presenter?.newPickup() }
        addDropButton.setOnClickListener { presenter?.newDrop() }

        whatField.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter?.openKeyboard() }
        whatField.setOnClickListener { presenter?.openKeyboard() }

        coinField.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter?.openKeyboard() }
        coinField.setOnClickListener { presenter?.openKeyboard() }

        rootView.setOnClickListener { presenter?.hideKeyboard() }

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
        super.onResume()
        presenter?.onResume()
    }

    override fun getContext(): Activity {
        return this
    }

    override fun updateAdapters(pickups: MutableList<NewOrderLocation>, drops: MutableList<NewOrderLocation>, numState: NumState) {

        pickupRecycler.adapter = LocationAdapter(this, numState, pickups) { location, index, view ->
            presenter?.openDetails(location, index, view)
        }

        dropRecycler.adapter = LocationAdapter(this, numState, drops) { location, index, view ->
            presenter?.openDetails(location, index, view)
        }

        if (pickups.size > 1) {
            touchHelperPickup?.attachToRecyclerView(pickupRecycler)
        } else {
            touchHelperPickup?.attachToRecyclerView(null)
        }

        if (drops.size > 1) {
            touchHelperDrop?.attachToRecyclerView(dropRecycler)
        } else {
            touchHelperDrop?.attachToRecyclerView(null)
        }

    }

    override fun getTransitionName(): String {
        return getString(R.string.number_address_transition)
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