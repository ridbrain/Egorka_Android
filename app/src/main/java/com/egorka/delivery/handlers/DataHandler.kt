package com.egorka.delivery.handlers

import android.content.Context

class DataHandler(val context: Context) {

    private val NAME = "EgorkaData"
    private val UUID = "EgorkaUserUIID"
    private val SECURE = "EgorkaUserSecure"
    private val FIREBASE = "EgorkaUserFCM"
    private val ERRORACTIVITY = "ErrorActivity"

    private val settings = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun isUserUUID() : Boolean {
        return getUserUUID() != null
    }

    fun setUserUUID(uuid: String) {
        val editor = settings.edit()
        editor.putString(UUID, uuid)
        editor.apply()
    }

    fun getUserUUID() : String? {
        return settings.getString(UUID, null)
    }

    fun setUserSecure(secure: String) {
        val editor = settings.edit()
        editor.putString(SECURE, secure)
        editor.apply()
    }

    fun getUserSecure() : String? {
        return settings.getString(SECURE, null)
    }

    fun setUserFCM(fcm: String) {
        val editor = settings.edit()
        editor.putString(FIREBASE, fcm)
        editor.apply()
    }

    fun getUserFCM() : String? {
        return settings.getString(FIREBASE, null)
    }

    fun setErrorActivityState(open: Boolean) {
        val editor = settings.edit()
        editor.putBoolean(ERRORACTIVITY, open)
        editor.apply()
    }

    fun getErrorActivityState() : Boolean {
        return settings.getBoolean(ERRORACTIVITY, false)
    }

}