package com.example.travira.auth

import android.content.Context
import android.content.SharedPreferences

object AuthManager {
    private const val PREF_NAME = "travira_auth"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USERNAME = "username"
    private const val KEY_ROLE = "user_role"
    private const val KEY_TOURIST_UID = "tourist_uid"
    
    private var token: String? = null
    private var username: String? = null
    private var role: String? = null
    private var touristUID: String? = null
    
    fun initialize(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        token = prefs.getString(KEY_TOKEN, null)
        username = prefs.getString(KEY_USERNAME, null)
        role = prefs.getString(KEY_ROLE, null)
        touristUID = prefs.getString(KEY_TOURIST_UID, null)
    }
    
    fun saveAuthData(context: Context, authToken: String, userName: String, userRole: String) {
        token = authToken
        username = userName
        role = userRole
        
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_TOKEN, authToken)
            .putString(KEY_USERNAME, userName)
            .putString(KEY_ROLE, userRole)
            .apply()
    }

    fun saveTouristUID(context: Context, uid: String) {
        touristUID = uid
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TOURIST_UID, uid).apply()
    }
    
    fun getAuthToken(): String? = token
    
    fun getUsername(): String? = username
    
    fun getRole(): String? = role

    fun getTouristUID(): String? = touristUID
    
    fun isLoggedIn(): Boolean = !token.isNullOrEmpty()
    
    fun logout(context: Context) {
        token = null
        username = null
        role = null
        touristUID = null
        
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
