package com.myapp.les_15

import android.content.SharedPreferences

class EncryptedPreferences(private val encryptedPreferences: SharedPreferences) {
    fun saveJwtToken(token: String) {
        encryptedPreferences.edit().putString(SharedPrefConst.JWT_TOKEN.name, token).apply()
    }
    fun getJwtToken(): String {
        return encryptedPreferences.getString(SharedPrefConst.JWT_TOKEN.name, "") ?: ""
    }
    enum class SharedPrefConst {
        JWT_TOKEN
    }

}