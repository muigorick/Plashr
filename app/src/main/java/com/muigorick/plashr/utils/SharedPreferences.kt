package com.muigorick.plashr.utils

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferences(context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val imageLayout: String?
        get() {
            return sharedPreferences.getString("key_image_layout", "")
        }

    val appTheme: String?
        get() {
            return sharedPreferences.getString("key_app_theme", "")
        }
}