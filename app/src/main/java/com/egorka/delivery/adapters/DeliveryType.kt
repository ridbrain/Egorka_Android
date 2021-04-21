package com.egorka.delivery.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R

class TypeDelivery(var type: Int, var descr: String, var price: Float, var image: Int)

class DeliveryType(var didDeselectItem: (Int) -> Unit) : RecyclerView.Adapter<DeliveryType.TypeDeliveryView>() {

    var types: List<TypeDelivery> = ArrayList()

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

        holder.txtName.text = type.descr
        holder.txtPrice.text = "${type.price} ₽"
        holder.image.setImageResource(type.image)

        holder.itemView.setOnClickListener { didDeselectItem(type.type) }

    }

    override fun getItemCount(): Int {
        return types.size
    }

}