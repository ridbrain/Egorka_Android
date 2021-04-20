package com.egorka.delivery.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.egorka.delivery.R
import com.egorka.delivery.entities.LocationType
import com.egorka.delivery.entities.NewOrderLocation

enum class NumState {
    Lite,
    Full
}

class LocationAdapter(
    val context: Activity,
    var numState: NumState,
    val locations: MutableList<NewOrderLocation>,
    val callback: (NewOrderLocation, Int, View) -> Unit): RecyclerView.Adapter<LocationAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var addressLabel: TextView = itemView.findViewById(R.id.addressLabel)
        var numLabel: TextView = itemView.findViewById(R.id.numTextView)
        var detailsLabel: TextView = itemView.findViewById(R.id.detailsLabel)
        var imgView: ImageView = itemView.findViewById(R.id.imgView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_location, parent, false))
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val location = locations[position]

        when (location.Type) {
            LocationType.Pickup -> {
                holder.numLabel.background = ContextCompat.getDrawable(context, R.drawable.background_round_red)
                holder.numLabel.text = "A${position+1}"
            }
            LocationType.Drop -> {
                holder.numLabel.background = ContextCompat.getDrawable(context, R.drawable.background_round_blue)
                holder.numLabel.text = "B${position+1}"
            }
        }

        when (numState) {
            NumState.Lite -> {
                holder.numLabel.layoutParams.width = context.resources.getDimension(R.dimen._11sdp).toInt()
                holder.numLabel.layoutParams.height = holder.numLabel.layoutParams.width
                holder.numLabel.text = ""
            }
            NumState.Full -> {
                holder.numLabel.layoutParams.width = context.resources.getDimension(R.dimen._19sdp).toInt()
                holder.numLabel.layoutParams.height = holder.numLabel.layoutParams.width
            }
        }

        if (location.Type == LocationType.Drop && locations.lastIndex == position) {
            holder.imgView.setImageResource(R.drawable.ic_flag)
        } else {
            holder.imgView.setImageResource(R.drawable.ic_down_arrow)
        }

        holder.addressLabel.text = location.Point?.Address
        holder.detailsLabel.text = "Указать детали"
        holder.detailsLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))

        location.Contact?.let {

            if (it.Name == null) { return }
            if (it.PhoneMobile == null) { return }

            holder.detailsLabel.text = "${it.Name}, ${it.PhoneMobile}"
            holder.detailsLabel.setTextColor(ContextCompat.getColor(context, R.color.colorGrayDark))

        }

        holder.itemView.setOnClickListener { callback(location, position, holder.numLabel) }

    }

}