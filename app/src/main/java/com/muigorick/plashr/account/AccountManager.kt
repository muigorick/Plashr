package com.muigorick.plashr.account

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.muigorick.plashr.utils.Plashr

class AccountManager : Application() {

    private var instance: AccountManager? = null

    private val preferenceName: String = "plashrLoginPreference"
    private val keyIsAuthorized: String = "authorized"
    private val keyLoginAccessToken: String = "accessToken"
    private val keyUserID: String = "userId"
    private val keyUsername: String = "username"
    private val keyFirstName: String = "firstName"
    private val keyLastName: String = "lastName"
    private val keyUserProfilePicURL: String = "profilePicURL"

    private val sharedPreferences: SharedPreferences =
        Plashr().getInstance().getSharedPreferences(preferenceName, Context.MODE_PRIVATE)!!
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun getInstance(): AccountManager? {
        if (instance == null) {
            synchronized(AccountManager::class.java) {
                if (instance == null) {
                    instance = AccountManager()
                }
            }
        }
        return instance
    }

    /**
     * Adds authorization status and access token to shared preferences when the user grants the app access to their Unsplash account.
     *
     * @param isAuthorized Boolean value of whether the user has authorized the account for login.
     *                     True if allowed and false if denied.
     *                     The boolean value is false by default until the user logs in.
     * @param accessToken String value of the access token that is used to make calls that utilize features that require you to be logged in.
     *                    The value is null by default until the user logs in.
     */
    fun addAuthorization(isAuthorized: Boolean, accessToken: String) {
        editor.putBoolean(keyIsAuthorized, isAuthorized)
        editor.putString(keyLoginAccessToken, accessToken)
        editor.commit()
    }

    /**
     * Adds all user details.
     *
     * @param userID This is the user's USER_ID that is to be stored.
     * @param username This is the user's unsplash username that is to be stored.
     * @param firstName This is the user's first name that is to be stored.
     * @param lastName This is the user's last name that is to be stored
     * @param profilePictureURL This is the user's PROFILE_PICTURE_URL that is to be stored.
     */
    fun addUserDetails(
        userID: String?,
        username: String?,
        firstName: String?,
        lastName: String?,
        profilePictureURL: String?
    ) {
        editor.putString(keyUserID, userID)
        editor.putString(keyUsername, username)
        editor.putString(keyFirstName, firstName)
        editor.putString(keyLastName, lastName)
        editor.putString(keyUserProfilePicURL, profilePictureURL)
        editor.commit()
    }

    /**
     * Adds all user details.
     */
    fun getUserDetails(
    ): String {
        return sharedPreferences.all.toString()
    }

    /**
     * Returns a Boolean value of the authorization status on whether the user has granted access to their Unsplash account.
     *
     * @return Boolean value of the authorization status. It returns false by default if the user is not logged in.
     */
    fun isAppAuthorized(): Boolean {
        return sharedPreferences.getBoolean(keyIsAuthorized, false)
    }

    /**
     * Adds the user details once the user logs in. These details are the USER_ID and the unsplash username.
     *
     * @param accessToken String value of the access token that is used to make calls that utilize features that require you to be logged in.
     *                    The value is null by default until the user logs in.
     */
    fun addAccessToken(accessToken: String) {
        editor.putString(keyLoginAccessToken, accessToken)
        editor.commit()
    }

    /**
     * Fetches the access token.
     *
     * @return Returns a String value of the access token that is used to make calls when the user is logged in.
     *         It returns null by default if the user is not logged in.
     */
    fun getAccessToken(): String? {
        return sharedPreferences.getString(keyLoginAccessToken, null)
    }

    /**
     * Adds the user details once the user logs in. These details are the USER_ID and the USERNAME.
     *
     * @param userID   String value of the user's USER_ID that is to be stored.
     * @param username String value of the user's unsplash username that is to be stored.
     */
    fun addUserIdAndUsername(userID: String, username: String) {
        editor.putString(keyUserID, userID)
        editor.putString(keyUsername, username)
        editor.commit()
    }

    /**
     * Adds the user's USER_ID once they log in.
     *
     * @param userID This is the user's USER_ID that is to be stored.
     */
    fun addUserID(userID: String) {
        editor.putString(keyUserID, userID)
        editor.commit()
    }

    /**
     * Gets the user's USER_ID.
     *
     * @return  Returns the user's USER_ID stored in the SharedPreferences.
     */
    fun getUserID(): String? {
        return sharedPreferences.getString(keyUserID, null)
    }

    /**
     * Adds the user's unsplash username into the SharedPreferences.
     *
     * @param username This is the user's unsplash username that is to be stored.
     */
    fun addUsername(username: String) {
        editor.putString(keyUsername, username)
        editor.commit()
    }

    /**
     * Gets the user's unsplash username .
     *
     * @return returns the user's unsplash username stored in the SharedPreferences.
     */
    fun getUsername(): String? {
        return sharedPreferences.getString(keyUsername, null)
    }

    /**
     * Adds the user's first and last names once they log in.
     *
     * @param firstName User's first name that is to be stored.
     * @param lastName User's last name that is to be stored
     */
    fun addFirstAndLastName(firstName: String, lastName: String) {
        editor.putString(keyFirstName, firstName)
        editor.putString(keyLastName, lastName)
        editor.commit()
    }


    /**
     * Gets the user's first name.
     *
     * @return  returns the user's first name stored in the SharedPreferences.
     */
    fun getFirstName(): String? {
        return sharedPreferences.getString(keyLastName, null)
    }

    /**
     * Gets the user's last name.
     *
     * @return  returns the user's last name stored in the SharedPreferences.
     */
    fun getLastName(): String? {
        return sharedPreferences.getString(keyLastName, null)
    }

    /**
     * Standalone method that gets the user's first and last name and combines them to form the full name.
     *
     * @return  returns the user's first name and last name stored in the SharedPreferences as their full name.
     */
    fun getName(): String {
        return sharedPreferences.getString(keyFirstName, null) + " " + sharedPreferences.getString(
            keyLastName,
            null
        )
    }

    /**
     * Standalone method that adds the user's PROFILE_PICTURE_URL once they log in.
     *
     * @param profilePictureURL This is the user's PROFILE_PICTURE_URL that is to be stored.
     */
    fun addProfilePictureURL(profilePictureURL: String) {
        editor.putString(keyUserProfilePicURL, profilePictureURL)
        editor.commit()
    }


    /**
     * Standalone method that adds the user's PROFILE_PICTURE_URL once they log in.
     *
     * @return  returns the user's PROFILE_PICTURE_URL stored in the SharedPreferences.
     */
    fun getProfilePictureURL(): String? {
        return sharedPreferences.getString(keyUserProfilePicURL, null)
    }

    /**
     * Logs out the current user and clears all the values that were stored when they logged in.
     * These values are the AUTHORIZATION_STATUS, ACCESS_TOKEN, USER_ID, USERNAME, USER_PROFILE_PIC_URL
     */
    fun logoutUser() {
        editor.putBoolean(keyIsAuthorized, false)
        editor.putString(keyLoginAccessToken, null)
        editor.putString(keyUserID, null)
        editor.putString(keyUsername, null)
        editor.putString(keyUserProfilePicURL, null)
        editor.commit()
    }
}