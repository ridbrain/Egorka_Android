package com.egorka.delivery.modules.currentOrderActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.google.android.gms.maps.SupportMapFragment

class OrderStatusActivity : AppCompatActivity(), OrderStatusActivityInterface {

    lateinit var presenter: OrderStatusPresenter
    lateinit var mapView: SupportMapFragment
    lateinit var recyclerViewAddresses: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_order)
        presenter = OrderStatusPresenter(this)
        presenter.onCreate()

        initView()
    }

    override fun initView() {
        mapView = supportFragmentManager.findFragmentById(R.id.map_order) as SupportMapFragment
        recyclerViewAddresses = findViewById(R.id.pickupRecycler)
        recyclerViewAddresses.layoutManager = LinearLayoutManager(this)
//        recyclerViewAddresses.adapter = AddAddressRecyclerAdapter(mutableListOf(
//            NewOrderLocation(Suggestion("", "Солнечная 6" ,
//                Point("Солнечная 6", null, null, null, null, null, null)),
//                LocationType.Pickup,
//                1)
//            ), "", ) { type, position, numberOfAddress ->
//
//        }
    }


}