package com.muigorick.plashr.ui.bottomNavigation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileFragmentViewModel : ViewModel() {
    val accessTokenFetched = MutableLiveData<Boolean>()
    val loggedUserProfileFetched = MutableLiveData<Boolean>()
    val loggedUserPublicProfileFetched = MutableLiveData<Boolean>()
    val restartApp = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val displayNotLoggedInLayout = MutableLiveData<Boolean>()

    fun loggedUserProfileFetched(fetchStatus: Boolean) {
        loggedUserProfileFetched.value = fetchStatus
    }

    fun loggedUserPublicProfileFetched(fetchStatus: Boolean) {
        loggedUserPublicProfileFetched.value = fetchStatus
    }

    fun accessTokenFetched(fetchStatus: Boolean) {
        accessTokenFetched.value = fetchStatus
    }

    fun restartApp(restartStatus: Boolean) {
        restartApp.value = restartStatus
    }

    fun message(loginBottomSheetMessage: String) {
        message.value = loginBottomSheetMessage
    }

    fun displayNotLoggedInLayout(loginStatus:Boolean){
        displayNotLoggedInLayout.value = loginStatus
    }

}