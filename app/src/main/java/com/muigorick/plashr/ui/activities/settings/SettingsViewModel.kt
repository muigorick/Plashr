package com.muigorick.plashr.ui.activities.settings

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DataStoreRepository(application)

    val readThemeFromDataStore = repository.readThemeFromDataStore.asLiveData()

    fun saveThemeToDataStore(theme: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveThemeToDataStore(appTheme = theme)
    }

    val readImageLayoutFromDataStore: LiveData<String> =
        repository.readImageLayoutFromDataStore.asLiveData()

    fun saveImageLayoutToDataStore(layout: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveImageLayoutToDataStore(imageLayout = layout)
    }

    private val restartMainActivity = MutableLiveData<Boolean>()

    fun getRestartMainActivity(): LiveData<Boolean> {
        return restartMainActivity
    }

    fun setRestartMainActivity(restartMainActivity: Boolean) {
        this.restartMainActivity.value = restartMainActivity
    }
}