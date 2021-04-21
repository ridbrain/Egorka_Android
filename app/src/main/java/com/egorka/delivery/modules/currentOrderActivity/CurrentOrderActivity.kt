package com.egorka.delivery.modules.currentOrderActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.adapters.LocationAdapter
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.entities.NewOrderLocation
import com.egorka.delivery.handlers.GoogleMapHandler
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_current_order.*

class CurrentOrderActivity : AppCompatActivity(), CurrentOrderActivityInterface {

    private var presenter: CurrentOrderPresenterInterface? = null
    private var mapDelegate: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_order)

        presenter = CurrentOrderPresenter(this)

        routeRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val mapView = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapView.getMapAsync { mapDelegate = it }

        backButton.setOnClickListener { onBackPressed() }

    }

    override fun onResume() {
        presenter?.onResume()
        super.onResume()
    }

    override fun onPause() {
        presenter?.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        presenter?.onBackPressed()
        super.onBackPressed()
    }

    override fun getContext(): Activity {
        return this
    }

    override fun setPolyline(polylineOptions: PolylineOptions) {

        val gMapHandler = GoogleMapHandler(this)
        val size = resources.getDimension(R.dimen._20sdp).toInt()
        val padding = resources.getDimension(R.dimen._40sdp).toInt()

        mapDelegate?.addPolyline(polylineOptions)
        mapDelegate?.addMarker(gMapHandler.getMarker(polylineOptions.points.first(), R.drawable.ic_pin_pickup, size))
        mapDelegate?.addMarker(gMapHandler.getMarker(polylineOptions.points.last(), R.drawable.ic_pin_drop, size))
        mapDelegate?.animateCamera(gMapHandler.getCameraPosition(polylineOptions.points, padding))

    }

    override fun updateAdapter(locations: MutableList<NewOrderLocation>, numState: NumState) {

        routeRecycler.adapter = LocationAdapter(this, numState, locations) { _, _, _ -> }

    }

}