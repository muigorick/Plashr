package com.muigorick.plashr.ui.activities.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsActivityViewModel : ViewModel() {
    private val restartMainActivity = MutableLiveData<Boolean>()

    fun getRefreshLayout(): LiveData<Boolean> {
        return restartMainActivity
    }

    fun setRefreshLayout(refreshLayout: Boolean) {
        this.restartMainActivity.value = refreshLayout
    }
}