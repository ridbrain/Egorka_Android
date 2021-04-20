package com.egorka.delivery.entities

class UserUUID {
    var ID: String?  = null
    var Secure: String? = null
    var Status: String? = null
}

class AnswerUser {
    var Time: String? = null
    var TimeStamp: Int? = null
    var Execution: Float? = null
    var Method: String? = null
    var Result: UserUUID? = null
}