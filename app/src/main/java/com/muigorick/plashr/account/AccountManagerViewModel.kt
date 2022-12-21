package com.muigorick.plashr.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AccountManagerViewModel(application: Application) : AndroidViewModel(application) {

    /*private val repository = AccountManager(application)

    private val readAuthorizationFromDataStore =
        repository.readAuthorizationFromDataStore().asLiveData()

    fun isAppAuthorized(): Boolean? {
        return readAuthorizationFromDataStore.value
    }

    val readAccessTokenFromDataStore = repository.readAccessTokenFromDataStore().asLiveData()


    fun getAuthorization(): Boolean {
        var authorization = false
        viewModelScope.launch {
            repository.readAuthorizationFromDataStore().collect {
                authorization = it
            }
        }
        return authorization
    }

    fun addAuthorizationToDatabase(isAuthorized: Boolean, accessToken: String) {
        viewModelScope.launch {
            repository.addAuthorizationToDataStore(
                isAuthorized = isAuthorized,
                accessToken = accessToken
            )
        }
    }

    fun addUserDetailsToDataStore(
        userID: String,
        username: String,
        firstName: String,
        lastName: String,
        profilePictureURL: String
    ) {
        viewModelScope.launch {
            repository.addUserDetailsToDataStore(
                userID,
                username,
                firstName,
                lastName,
                profilePictureURL
            )
        }
    }

    val readUserIdFromDataStore = repository.readUserIdFromDataStore.asLiveData()
    val readUsernameFromDataStore = repository.readUsernameFromDataStore.asLiveData()
    val readUserFirstNameFromDataStore = repository.readUserFirstNameFromDataStore.asLiveData()
    val readUserLastNameFromDataStore = repository.readUserLastNameFromDataStore.asLiveData()
    val readProfilePicURLFromDataStore = repository.readProfilePicURLFromDataStore.asLiveData()
    suspend fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }*/
}