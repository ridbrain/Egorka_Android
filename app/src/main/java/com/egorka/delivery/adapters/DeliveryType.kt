package com.egorka.delivery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.entities.Delivery

class TypeData(type: com.egorka.delivery.entities.DeliveryType) {

    var icon: Int? = null
    var label: String? = null

    init {
        when (type) {
            com.egorka.delivery.entities.DeliveryType.Car -> {
                icon = R.drawable.ic_car
                label = "Легковой"
            }
            com.egorka.delivery.entities.DeliveryType.Walk -> {
                icon = R.drawable.ic_leg
                label = "Пеший"
            }
        }
    }

}

class DeliveryType(var didDeselectItem: (Delivery) -> Unit) : RecyclerView.Adapter<DeliveryType.TypeDeliveryView>() {

    var types: List<Delivery> = ArrayList()

    class TypeDeliveryView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView = itemView.findViewById(R.id.typeName)
        var txtPrice: TextView = itemView.findViewById(R.id.price)
        var image: ImageView = itemView.findViewById(R.id.typeDeliveryImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeDeliveryView {
        return TypeDeliveryView(LayoutInflater.from(parent.context).inflate(R.layout.recycler_delivery_type, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TypeDeliveryView, position: Int) {

        val type = types[position]
        val data = TypeData(type.Type!!)

        holder.txtName.text = data.label
        holder.txtPrice.text = "${type.Result?.TotalPrice?.Total?.div(100)} ₽"
        holder.image.setImageResource(data.icon!!)

        holder.itemView.setOnClickListener { didDeselectItem(type) }

    }

    override fun getItemCount(): Int {
        return types.size
    }

}