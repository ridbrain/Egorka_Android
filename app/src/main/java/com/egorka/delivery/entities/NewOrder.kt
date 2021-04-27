package com.egorka.delivery.entities

class Dictionary {

    var Time: String? = null
    var TimeStamp: Int? = null
    var Execution: Float? = null
    var Method: String? = null
    var Result: DictionaryResult? = null

    class Suggestion {
        var ID: String? = null
        var Name: String? = null
        var Point: Point? = null
    }

    class DictionaryResult {
        var Cached: Boolean? = null
        var Suggestions: List<Suggestion>? = null
    }

}

class Point {

    var Address: String? = null
    var Code: String? = null
    var Latitude: Double? = null
    var Longitude: Double? = null
    var Entrance: Int? = null
    var Floor: Int? = null
    var Room: Int? = null

}

class OrderLocation(routeOrder: Int?, type: LocationType?, suggestion: Dictionary.Suggestion) {

    var ID: String? = null
    var Key: String? = null
    var Date: String? = null
    var Type: LocationType? = type
    var Route: Int? = null
    var RouteOrder: Int? = null
    var Point: Point? = null
    var Contact: Contact? = null
    var Message: String? = null

    init {

        val point = suggestion.Point
        point?.Code = suggestion.ID
        point?.Address = suggestion.Name

        ID = ""
        Key = ""
        Route = 1
        RouteOrder = routeOrder
        Point = point
        Message = ""

    }

}

class Delivery {

    var Time: String? = null
    var TimeStamp: Int? = null
    var Execution: Float? = null
    var Method: String? = null
    var Result: DeliveryResult? = null
    var Type: DeliveryType? = null

    class DeliveryResult {

        var ID: String? = null
        var Date: Int? = null
        var DateUpdate: Int? = null
        var Stage: Boolean? = null
        var RecordNumber: Int? = null
        var RecordPIN: Int? = null
        var RecordDate: String? = null
        var RecordDateStamp: Int? = null
        var Locations: MutableList<OrderLocation>? = null
        var TotalPrice: TotalPrice? = null

    }

    class TotalPrice {
        var Base: Int? = null
        var Ancillary: Int? = null
        var Discount: Int? = null
        var Compensation: Int? = null
        var Bonus: Int? = null
        var Tip: Int? = null
        var Total: Int? = null
        var Currency: String? = null
    }

    fun restoreIndex() {

        var index = 1

        this.Result?.Locations?.forEach { location ->

            location.ID = "${location.Type.toString()}-${index}"
            location.Route = 1
            location.RouteOrder = index

            index++

        }

    }

    fun updateLocations(pickups: List<OrderLocation>, drops: List<OrderLocation>) {

        this.Result?.Locations = mutableListOf()

        pickups.forEach { location -> this.Result?.Locations?.add(location) }
        drops.forEach { location -> this.Result?.Locations?.add(location) }

    }

}

class Contact {
    var Name: String? = null
    var Department: String? = null
    var PhoneMobile: String? = null
    var PhoneOffice: String? = null
    var PhoneOfficeAdd: String? = null
    var EmailPersonal: String? = null
    var EmailOffice: String? = null
}

enum class LocationType {
    Pickup,
    Drop
}

enum class DeliveryType {
    Walk,
    Car
}