package com.egorka.delivery.modules.currentOrderActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egorka.delivery.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_current_order.*

class CurrentOrderActivity : AppCompatActivity(), CurrentOrderActivityInterface {

    lateinit var presenter: CurrentOrderPresenterInterface

    private var mapDelegate: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_order)

        presenter = CurrentOrderPresenter(this)
        presenter.onCreate()

        val mapView = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapView.getMapAsync { mapDelegate = it }

        backButton.setOnClickListener { onBackPressed() }

    }

    override fun onResume() {
        presenter.onResume()
        super.onResume()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
        super.onBackPressed()
    }

    override fun getContext(): Activity {
        return this
    }

}