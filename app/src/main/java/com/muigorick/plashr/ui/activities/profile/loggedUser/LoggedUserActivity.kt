package com.muigorick.plashr.ui.activities.profile.loggedUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.muigorick.plashr.R

class LoggedUserActivity : AppCompatActivity() {

    private lateinit var viewModel: LoggedUserActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_user)

        viewModel = ViewModelProvider(this).get(LoggedUserActivityViewModel::class.java)
    }
}