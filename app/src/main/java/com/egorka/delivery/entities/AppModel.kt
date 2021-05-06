package com.egorka.delivery.entities

interface MainModelInterface {
    var newOrder: Delivery?
    var details: OrderLocation?
    var detailsIndex: Int?
    var marketplaces: List<OrderLocation>?
}

class MainModel: MainModelInterface {
    override var newOrder: Delivery? = null
    override var details: OrderLocation? = null
    override var detailsIndex: Int? = null
    override var marketplaces: List<OrderLocation>? = null
}
