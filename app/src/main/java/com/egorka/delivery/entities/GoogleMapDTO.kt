package com.egorka.delivery.entities

class GoogleMapDTO {
    var routes: ArrayList<Routes>? = null
}

class Routes {
    var legs:  ArrayList<Legs>? = null
}

class Legs {
    var distance: Distance? = null
    var duration: Duration? = null
    var end_address: String? = null
    var start_address: String? = null
    var end_location: Location? = null
    var start_location: Location? = null
    var steps: ArrayList<Steps>? = null
}

class Steps {
    var distance: Distance? = null
    var duration: Distance? = null
    var end_address: String? = null
    var start_address: String? = null
    var end_location: Location? = null
    var start_location: Location? = null
    var polyline: PolyLine? = null
    var travel_mode: String? = null
    var maneuver: String? = null
}

class Duration {
    var text: String? = null
    var value: Int? = null
}

class Distance {
    var text: String? = null
    var value: Int? = null
}

class PolyLine {
    var points: String? = null
}

class Location {
    var lat: String? = null
    var lng: String? = null
}