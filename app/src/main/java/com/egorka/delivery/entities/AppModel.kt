package com.egorka.delivery.entities

interface MainModelInterface {
    var pickups: MutableList<NewOrderLocation>?
    var drops: MutableList<NewOrderLocation>?
    var details: NewOrderLocation?
    var detailsIndex: Int?
}

class MainModel: MainModelInterface {
    override var pickups: MutableList<NewOrderLocation>? = null
    override var drops: MutableList<NewOrderLocation>? = null
    override var details: NewOrderLocation? = null
    override var detailsIndex: Int? = null
}
