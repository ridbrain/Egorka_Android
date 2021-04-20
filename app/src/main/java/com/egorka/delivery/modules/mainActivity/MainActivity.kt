package com.egorka.delivery.modules.mainActivity

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.adapters.AddressAdapter
import com.egorka.delivery.adapters.DeliveryType
import com.egorka.delivery.adapters.TypeDelivery
import com.egorka.delivery.delegates.EditTextWatcher
import com.egorka.delivery.entities.Suggestion
import com.egorka.delivery.handlers.BottomSheetHandler
import com.egorka.delivery.services.BottomState
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.suggestionsRecycler

class MainActivity: AppCompatActivity(), MainActivityInterface, ActivityCompat.OnRequestPermissionsResultCallback {

    lateinit var presenter: MainPresenterInterface

    private var mapDelegate: GoogleMap? = null
    private var mapCoordinate: CameraPosition? = null
    private var addressAdapter: AddressAdapter? = null
    private var typeAdapter: DeliveryType? = null
    private var bottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private var bottomHeight: Int? = null
    private var mapNotify = true
    private var cameraPosition: CameraUpdate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)
        presenter.onCreate()

        menuButton.setOnClickListener { drawer_layout.openDrawer(menuView) }
        menuView.setNavigationItemSelectedListener { MainMenu(this, drawer_layout).pressButton(it.itemId); true }

        pickupEditText.addTextChangedListener(EditTextWatcher { presenter.textDidChange(it) })
        pickupEditText.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter.selectTextField() }
        pickupEditText.setOnClickListener { presenter.selectTextField() }

        dropEditText.addTextChangedListener(EditTextWatcher { presenter.textDidChange(it) })
        dropEditText.setOnFocusChangeListener { _, isFocused -> if (isFocused) presenter.selectTextField() }
        dropEditText.setOnClickListener { presenter.selectTextField() }

        val mapView = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapView.getMapAsync { startGoogleMap(it) }

        bottomSheet = BottomSheetBehavior.from(mainBottomSheet)
        bottomSheet?.isHideable = true
        bottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheet?.addBottomSheetCallback(BottomSheetHandler { presenter.bottomStateChanged(it)})

        pickupFieldButton.setOnClickListener { presenter.pressPickupFieldButton() }
        dropFieldButton.setOnClickListener { presenter.pressDropFieldButton() }

        bottomHeight = resources.displayMetrics.heightPixels - resources.getDimension(R.dimen._70sdp).toInt()
        bottomHeight?.let { mainBottomSheet.layoutParams.height = (it) }

        addressAdapter = AddressAdapter { presenter.selectAddress(it) }

        suggestionsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        suggestionsRecycler.adapter = addressAdapter

        typeAdapter = DeliveryType { presenter.selectTypeDelivery(it) }

        deliveryTypeRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        deliveryTypeRecycler.adapter = typeAdapter

    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    private fun startGoogleMap(map: GoogleMap) {

        mapDelegate = map

        mapDelegate?.setOnCameraIdleListener {

            if (mapNotify) {
                mapCoordinate = mapDelegate?.cameraPosition
                presenter.changeCameraPosition()
            }

            mapNotify = true

        }

        mapDelegate?.setOnMapClickListener { presenter.hideKeyboard() }

    }

    override fun getMapLocation(): CameraPosition? {
        return mapCoordinate
    }

    override fun getContext(): Activity {
        return this
    }

    override fun setAddress(field: FocusState, address: String) {
        when (field) {
            FocusState.First -> {
                pickupEditText.setText(address)
                pickupEditText.clearFocus()
            }
            FocusState.Second -> {
                dropEditText.setText(address)
                dropEditText.clearFocus()
            }
        }
    }

    override fun setMapRegion(latLng: LatLng) {
        mapDelegate?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    override fun setSuggestions(suggestion: List<Suggestion>) {
        addressAdapter?.suggestions = suggestion
        addressAdapter?.notifyDataSetChanged()
    }

    override fun setBottomSheetState(state: BottomState) {

        when (state) {
            BottomState.Small -> {
                bottomSheet?.peekHeight = resources.getDimension(R.dimen._120sdp).toInt()
                bottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            BottomState.Medium -> {
                bottomSheet?.peekHeight = resources.getDimension(R.dimen._185sdp).toInt()
                bottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            BottomState.Big -> {
                bottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        setMapPadding(bottomSheet!!.peekHeight)

    }

    private fun setMapPadding(bottom: Int) {
        mapDelegate?.setPadding(10, resources.getDimension(R.dimen._80sdp).toInt(), 10, bottom + 30)
    }

    override fun showSuggestionsRecyclerView(show: Boolean) {
        suggestionsRecycler.isVisible = show
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter.requestPermissions(requestCode)
    }

    override fun getFocusField(): FocusState {
        return if (pickupEditText.isFocused) FocusState.First else FocusState.Second
    }

    override fun showButtons(show: Boolean) {
        deliveryTypeRecycler.isVisible = show
    }

    override fun setPolyline(polylineOptions: PolylineOptions) {

        val size = resources.getDimension(R.dimen._30sdp).toInt()

        val builder = LatLngBounds.Builder()
        val pickup = MarkerOptions().position(LatLng(polylineOptions.points.first().latitude, polylineOptions.points.first().longitude))
        val drop = MarkerOptions().position(LatLng(polylineOptions.points.last().latitude, polylineOptions.points.last().longitude))

        polylineOptions.points.forEach { builder.include(it) }
        pickup.icon(BitmapDescriptorFactory.fromBitmap(ContextCompat.getDrawable(this, R.drawable.ic_pin_pickup)!!.toBitmap(size, size, null)))
        drop.icon(BitmapDescriptorFactory.fromBitmap(ContextCompat.getDrawable(this, R.drawable.ic_pin_drop)!!.toBitmap(size, size, null)))

        cameraPosition = CameraUpdateFactory.newLatLngBounds(builder.build(), size)

        mapDelegate?.addPolyline(polylineOptions)
        mapDelegate?.addMarker(pickup)
        mapDelegate?.addMarker(drop)

        presenter.didRouteLaid()

        mapDelegate?.animateCamera(cameraPosition)
        mapNotify = false

    }

    override fun defaultAnimateCamera() {
        mapDelegate?.animateCamera(cameraPosition)
        mapNotify = false
    }

    override fun setFocus(field: FocusState, size: Int) {
        when(field) {
            FocusState.First -> {
                pickupEditText.requestFocus()
                pickupEditText.setSelection(size)
            }
            FocusState.Second -> {
                pickupEditText.clearFocus()
                dropEditText.requestFocus()
                dropEditText.setSelection(size)
            }
        }
    }

    override fun deleteFocus(focusState: FocusState) {
        when(focusState) {
            FocusState.First -> pickupEditText.clearFocus()
            FocusState.Second -> dropEditText.clearFocus()
        }
    }

    override fun getPickupText(): String {
        return pickupEditText.text.toString()
    }

    override fun getDropText(): String {
        return dropEditText.text.toString()
    }

    override fun showPin(show: Boolean) {
        pin.isVisible = show
    }

    override fun deletePolyline() {
        mapDelegate?.clear()
    }

    override fun showDropEditText(show: Boolean) {
        dropFrameLayout.isVisible = show
    }

    override fun changeIconFirstField(edit: Boolean) {
        if (edit) {
            pickupFieldButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_remove))
        } else {
            pickupFieldButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cross_hair))
        }
    }

    override fun showIconSecondField(show: Boolean) {
        dropFieldButton.isVisible = show
    }

    override fun showChangeAlert(callback: (Boolean) -> Unit) {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("Внимание")
            .setMessage("Хотите изменить маршрут доставки?")
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                callback(true)
            }
            .setNegativeButton("Нет") { _, _ ->
                callback(false)
            }

        builder.create().show()

    }

    override fun showWarning() {

        val builder = AlertDialog.Builder(this, R.style.AlertDialog)

        builder.setTitle("Внимание")
            .setMessage("Вы указали два одинаковых адреса, построение маршрута невозможно.")
            .setCancelable(false)
            .setPositiveButton("Понятно") { _, _ -> }

        builder.create().show()

    }

    override fun updateTypeAdapter(types: List<TypeDelivery>) {
        typeAdapter?.types = types
        typeAdapter?.notifyDataSetChanged()
    }

    override fun clearFocus() {
        pickupEditText.clearFocus()
        dropEditText.clearFocus()
    }

}

