package com.muigorick.plashr.ui.activities.photos

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.muigorick.plashr.databinding.ActivitySinglePhotoFullViewBinding

class SinglePhotoFullViewActivity : AppCompatActivity() {

    //TODO Make the activity fullscreen. Maybe add a translucent status and navigation bar.

    private lateinit var binding: ActivitySinglePhotoFullViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePhotoFullViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle: Bundle? = intent.extras
        val photoUrl = bundle!!.getString("photo_url")
        Glide.with(this)
            .load(photoUrl)
            .into(binding.imageView)
    }

}