package com.egorka.delivery.entities

class Suggestion {
    var ID: String? = null
    var Name: String? = null
    var Point: Point? = null
}

class Result {
    var Cached: Boolean? = null
    var Suggestions: List<Suggestion>? = null
}

class Dictionary {
    var Time: String? = null
    var TimeStamp: Int? = null
    var Execution: Float? = null
    var Method: String? = null
    var Result: Result? = null
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

class NewOrderLocation(num: Int?, type: LocationType?, suggestion: Suggestion) {

    var ID: String? = null
    var Key: String? = null
    var Num: Int? = num
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
        Route = 0
        RouteOrder = 0
        Point = point
        Message = ""

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