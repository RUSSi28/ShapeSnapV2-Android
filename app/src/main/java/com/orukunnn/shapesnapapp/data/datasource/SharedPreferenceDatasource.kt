package com.orukunnn.shapesnapapp.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferenceDatasource(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        sharedPreferences.edit { putString(KEY_USER_ID, userId) }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun clear() {
        sharedPreferences.edit { clear() }
    }

    companion object {
        private const val PREF_NAME = "shape_snap_pref"
        private const val KEY_USER_ID = "user_id"
    }
}
