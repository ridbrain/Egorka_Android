package com.egorka.delivery.entities

import android.content.Context
import com.egorka.delivery.handlers.DataHandler

class Parameters(context: Context, method: String, body: Any, params: Any) {
    val Auth = Auth(context)
    var Method = method
    var Body = body
    val Params = params
}

class Auth(context: Context) {
    val Type = "Android"
    val UserUUID = DataHandler(context).getUserUUID() ?: ""
}

class Query(query: String) {
    var Query = query
}

class Params {
    val Compress = "GZip"
    val Language = "RU"
}

class DeliveryCalc(type: DeliveryType, locations: List<OrderLocation>) {
    val Type = type.toString()
    val Locations = locations
}