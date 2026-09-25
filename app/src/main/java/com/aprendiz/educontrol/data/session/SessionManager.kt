package com.aprendiz.educontrol.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveSession(userId: Long, role: String, userName: String, email: String) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_ROLE, role)
            putString(KEY_USER_NAME, userName)
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getCurrentUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    fun getCurrentRole(): String = prefs.getString(KEY_ROLE, ROLE_STUDENT) ?: ROLE_STUDENT

    fun getCurrentUserName(): String = prefs.getString(KEY_USER_NAME, "Usuario") ?: "Usuario"

    fun getCurrentEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "educontrol_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ROLE = "role"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        const val ROLE_STUDENT = "STUDENT"
        const val ROLE_TEACHER = "TEACHER"
    }
}
