package com.egorka.delivery.modules.marketMap

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egorka.delivery.R
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.handlers.GoogleMapHandler
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_market_map.*

class MarketMapActivity : AppCompatActivity(), MarketMapActivityInterface {

    private var presenter: MarketMapPresenterInterface? = null
    private var mapDelegate: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_map)

        val mapView = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapView.getMapAsync { startGoogleMap(it) }

        backButton.setOnClickListener { onBackPressed() }

    }

    private fun startGoogleMap(map: GoogleMap) {

        map.isBuildingsEnabled = true
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isRotateGesturesEnabled = false

        mapDelegate = map

        presenter = MarketMapPresenter(this)

    }

    override fun getContext(): Activity {
        return this
    }

    override fun setPoints(points: List<OrderLocation>) {

        if (mapDelegate == null) { return }

        val gMapHandler = GoogleMapHandler(this)
        val size = resources.getDimension(R.dimen._30sdp).toInt()
        val bounds = ArrayList<LatLng>()

        points.forEach { point ->

            val bound = LatLng(point.Point!!.Latitude!!, point.Point!!.Longitude!!)

            mapDelegate?.addMarker(gMapHandler.getMarker(bound, R.drawable.ic_pin_pickup, size).title(point.Point!!.Name!!))
            bounds.add(bound)

        }

        mapDelegate?.animateCamera(gMapHandler.getCameraPosition(bounds, size))
        mapDelegate?.setOnInfoWindowClickListener { marker ->

            val locations = points
                .filter { it.Point!!.Latitude == marker.position.latitude }
                .filter { it.Point!!.Longitude == marker.position.longitude }

            if (locations.isNotEmpty()) {
                presenter?.selectLocation(locations[0])
            }

        }

    }

}