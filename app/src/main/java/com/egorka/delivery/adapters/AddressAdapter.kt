package com.egorka.delivery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.entities.Suggestion

open class AddressAdapter(val myCallback: (address: Suggestion) -> Unit
) : RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    var suggestions = listOf<Suggestion>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.addressLabel)
        lateinit var address: Suggestion
        lateinit var myCallback: (address: Suggestion) -> Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_address, parent, false))
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val address = suggestions[position]

        holder.text.text = address.Name
        holder.address = address
        holder.myCallback = myCallback
        holder.text.setOnClickListener { myCallback(address) }

    }

}