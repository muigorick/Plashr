package com.muigorick.plashr.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel

class SharedViewModel(context: Context) :
    ViewModel() {
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