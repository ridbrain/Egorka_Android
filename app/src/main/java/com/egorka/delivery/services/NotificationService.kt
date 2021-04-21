package com.egorka.delivery.services

import com.egorka.delivery.handlers.DataHandler
import com.google.firebase.messaging.FirebaseMessagingService

class NotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        DataHandler(this).setUserFCM(token)
        super.onNewToken(token)
    }

}