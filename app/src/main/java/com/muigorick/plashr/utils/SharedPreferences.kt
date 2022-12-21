package com.muigorick.plashr.utils

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muigorick.plashr.ui.activities.settings.SettingsViewModel

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