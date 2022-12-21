package com.muigorick.plashr.ui.activities.profile.universalProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.muigorick.plashr.R
import com.muigorick.plashr.ui.activities.profile.loggedUser.LoggedUserActivityViewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: UserProfileActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        viewModel = ViewModelProvider(this).get(UserProfileActivityViewModel::class.java)
    }
}