package com.ajax.ajaxtestassignment.data.preferences

import android.content.Context
import androidx.preference.PreferenceManager

class UserPreferencesImpl(context: Context): UserPreferences {
    companion object {
        const val DATA_LOADED = "data_loaded"
    }

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override var isDataLoaded: Boolean
        get() = prefs.getBoolean(DATA_LOADED, false)
        set(value) {
            prefs.edit().putBoolean(DATA_LOADED, value).apply()
        }
}