package com.example.travira.auth

import android.content.Context
import android.content.SharedPreferences

object AuthManager {
    private const val PREF_NAME = "travira_auth"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USERNAME = "username"
    private const val KEY_ROLE = "user_role"
    
    private var token: String? = null
    private var username: String? = null
    private var role: String? = null
    
    fun initialize(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        token = prefs.getString(KEY_TOKEN, null)
        username = prefs.getString(KEY_USERNAME, null)
        role = prefs.getString(KEY_ROLE, null)
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
    
    fun getAuthToken(): String? = token
    
    fun getUsername(): String? = username
    
    fun getRole(): String? = role
    
    fun isLoggedIn(): Boolean = !token.isNullOrEmpty()
    
    fun logout(context: Context) {
        token = null
        username = null
        role = null
        
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
