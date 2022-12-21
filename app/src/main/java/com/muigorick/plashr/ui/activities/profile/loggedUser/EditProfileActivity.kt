package com.muigorick.plashr.ui.activities.profile.loggedUser

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.muigorick.plashr.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.editProfileActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.editProfileActivityToolbar.setNavigationOnClickListener { onBackPressed() }

        viewModel = ViewModelProvider(this).get(EditProfileActivityViewModel::class.java)
    }

}