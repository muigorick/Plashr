package com.muigorick.plashr.ui.activities.photos

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.muigorick.plashr.R
import com.muigorick.plashr.account.AccountManager
import com.muigorick.plashr.actions.PhotoActions
import com.muigorick.plashr.adapters.PhotoTagsRecyclerViewAdapter
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.dataModels.photos.PhotoTags
import com.muigorick.plashr.databinding.ActivitySinglePhotoDetailsBinding
import com.muigorick.plashr.network.AppModule
import com.muigorick.plashr.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SinglePhotoDetailsActivity : AppCompatActivity(),
    PhotoTagsRecyclerViewAdapter.OnPhotoTagClickListener, PhotoActions.OnPhotoActionsListener {

    private lateinit var binding: ActivitySinglePhotoDetailsBinding
    private val viewModel: SinglePhotoDetailsActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        if (intent?.extras != null) {
            Log.i("SINGLE PHOTO DETAILS", "onNewIntent: ${intent.data}")

            val photo = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("photo", Photo::class.java)
            } else {
                intent.getParcelableExtra<Photo>("photo")
            }

            if (photo != null) {

                Log.i("PHOTO BUNDLE TAG", "INTENT = $photo")

                viewModel.setPhotoDetails(
                    photoId = photo.id,
                    photoURL = photo.urls?.regular,
                    photoDimensions = "${photo.width} x ${photo.height}",
                    photoDescription = photo.description,
                    photoOwnerFullName = photo.user?.name,
                    photoOwnerUsername = photo.user?.username,
                    photoOwnerProfilePic = photo.user?.profileImage?.medium,
                    photoDateCreated = photo.dateUploaded,
                    photoLikeCount = photo.likes,
                    photoLikedByUser = photo.likedByUser,
                    photoCameraMake = null,
                    photoCameraModel = null,
                    photoCameraExposure = null,
                    photoCameraAperture = null,
                    photoCameraFocalLength = null,
                    photoCameraISO = null,
                    photoLocation = null
                )
                photo.id?.let { getPhotoDetails(photoID = it) }
                viewModel.photoLoaded.value = false
            }
        } else {
            getPhotoDetails(
                photoID = Utils(context = this@SinglePhotoDetailsActivity).handleDeeplinkIntent(
                    intent = intent
                )!!.pathSegments[1]
            )
        }

        setOnClickListeners()
        setObservers()
    }

    /**
     * Sets the click listeners for all the clickable elements in the layout.
     * The clickable elements are:
     *  1. Like Button
     *  2. Download Button
     *  3. Collect Button
     *  4. View on Unsplash Button
     *  5. Share Button
     *  6. ImageView that holds the photo
     *  7. Individual photo tags - These are held in the viewmodel as a list and displayed in a recycler view.
     *  8. User profile - This includes the profile picture, full name as well as the username
     */
    private fun setOnClickListeners() {
        binding.imageView.setOnClickListener {
            if (viewModel.photo.value != null) {
                Log.i("PHOTO ON CLICK", "photo is not null")
                viewModel.photo.value?.let { photo -> navigateToSinglePhotoFullView(photo = photo) }
            } else {
                Log.i("PHOTO ON CLICK", "photo is null")
            }
        }

        binding.likeButton.setOnClickListener {
            if (AccountManager().isAppAuthorized()) {
                when (viewModel.photo.value?.likedByUser) {
                    false -> {
                        PhotoActions(this, viewModel.photo.value!!, this).likePhoto()
                    }
                    true -> {
                        PhotoActions(this, viewModel.photo.value!!, this).unlikePhoto()
                    }
                    else -> {}
                }

            } else {
                Toast.makeText(
                    this,
                    "LIKE IMAGE BUTTON CLICKED. USER NOT AUTHORIZED",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Collects the photo to the users desired collection
        binding.collectButton.setOnClickListener {
            if (AccountManager().isAppAuthorized()) {
                Toast.makeText(
                    this,
                    "COLLECT IMAGE BUTTON CLICKED. USER AUTHORIZED",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "COLLECT IMAGE BUTTON CLICKED. USER NOT AUTHORIZED",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Downloads the photo
        binding.downloadButton.setOnClickListener {
            Toast.makeText(this, "DOWNLOAD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
        }

        // View the photo on Unsplash
        binding.viewOnUnsplashButton.setOnClickListener {
            if (viewModel.photo.value?.attributionUrl != null) {
                Utils(this@SinglePhotoDetailsActivity).openWebPage(
                    url = viewModel.photo.value!!.attributionUrl,
                    packageManager = packageManager
                )
            }
        }

        // Shares the photo
        binding.shareButton.setOnClickListener {
            if (viewModel.photo.value != null) {
                PhotoActions(this, viewModel.photo.value!!, this).sharePhoto()
            }
        }

    }

    /**
     * Sets all our observers.
     *
     */
    private fun setObservers() {
        viewModel.photo.observe(this) { photo ->
            viewModel.setPhotoDetails(
                photoId = photo.id,
                photoURL = photo.urls?.regular,
                photoDimensions = "${photo.width} x ${photo.height}",
                photoDescription = photo.description,
                photoOwnerFullName = photo.user?.name,
                photoOwnerUsername = photo.user?.username,
                photoOwnerProfilePic = photo.user?.profileImage?.medium,
                photoDateCreated = photo.dateUploaded,
                photoLikeCount = photo.likes,
                photoLikedByUser = photo.likedByUser,
                photoCameraMake = photo.exif?.make,
                photoCameraModel = photo.exif?.model,
                photoCameraExposure = photo.exif?.exposure_time,
                photoCameraAperture = photo.exif?.aperture,
                photoCameraFocalLength = photo.exif?.focal_length,
                photoCameraISO = photo.exif?.iso,
                photoLocation = photo.location?.formattedLocation
            )
        }
        viewModel.photoDimensions.observe(this) {
            if (it != null) {
                binding.dimensionsTxt.text = it
            }
        }

        viewModel.photoURL.observe(this) { photoURL ->
            if (photoURL != null) {
                viewModel.photoLoaded.observe(this) { photoIsLoaded ->
                    if (photoIsLoaded == false) {
                        Glide.with(this@SinglePhotoDetailsActivity)
                            .load(photoURL)
                            .placeholder(viewModel.photo.value?.photoColor)
                            .into(binding.imageView)
                        viewModel.photoLoaded.value = true
                    }
                }
            }
        }

        viewModel.photoDescription.observe(this) {
            when (it) {
                null -> {
                    binding.descriptionTxt.visibility = View.GONE
                }
                else -> {
                    binding.descriptionTxt.text = it
                    binding.descriptionTxt.visibility = View.VISIBLE
                }
            }
        }

        viewModel.photoOwnerFullName.observe(this) {
            if (it != null) {
                binding.userFullNameTxt.text = it
            }
        }

        viewModel.photoOwnerUsername.observe(this) {
            if (it != null) {
                binding.userNameTxt.text = getString(R.string.username, it)
            }
        }

        viewModel.photoOwnerProfilePic.observe(this) {
            if (it != null) {
                Glide.with(this@SinglePhotoDetailsActivity)
                    .load(it)
                    .into(binding.profilePicCircleImageView)
            }
        }

        viewModel.photoDateCreated.observe(this) { date ->
            if (date != null) {
                binding.publishedDateTxt.text = getString(R.string.publish_date, date)
            }
        }

        viewModel.photoLikeCount.observe(this) {

        }

        viewModel.photoLikedByUser.observe(this) { liked ->
            when (liked) {
                true -> {
                    binding.likePhotoIconImageView.apply {
                        setImageResource(R.drawable.ic_like_filled_24)
                    }
                }
                false -> {
                    binding.likePhotoIconImageView.apply {
                        setImageResource(R.drawable.ic_like_border_24)
                    }
                }
            }
        }

        viewModel.photoCameraMake.observe(this) {
            if (it != null) {
                binding.cameraMakeTxt.text = it
            } else {
                binding.cameraMakeTxt.text = "--"
            }
        }

        viewModel.photoCameraModel.observe(this) {
            if (it != null) {
                binding.cameraModelTxt.text = it
            } else {
                binding.cameraModelTxt.text = "--"
            }
        }

        viewModel.photoCameraExposure.observe(this) {
            if (it != null) {
                binding.photoExposureTxt.text = this.getString(R.string.shutter_speed_count, it)
            } else {
                binding.photoExposureTxt.text = "--"
            }
        }

        viewModel.photoCameraAperture.observe(this) {
            if (it != null) {
                binding.photoApertureTxt.text = this.getString(R.string.aperture_f, it)
            } else {
                binding.photoApertureTxt.text = "--"
            }
        }

        viewModel.photoCameraFocalLength.observe(this) {
            if (it != null) {
                binding.photoFocalLengthTxt.text = this.getString(R.string.focal_length_mm, it)
            } else {
                binding.photoFocalLengthTxt.text = "--"
            }
        }

        viewModel.photoCameraISO.observe(this) {
            if (it != null) {
                binding.photoIsoTxt.text = it.toString()
            } else {
                binding.photoIsoTxt.text = "--"
            }
        }

        viewModel.photoLocation.observe(this) {
            when (it) {
                null -> {
                    binding.locationTxt.visibility = View.GONE
                    binding.locationIcon.visibility = View.GONE
                }
                else -> {
                    binding.locationTxt.text = it
                    binding.locationTxt.visibility = View.VISIBLE
                    binding.locationIcon.visibility = View.VISIBLE
                }
            }
        }

    }

    /**
     * Navigate to Single Photo FullView
     *
     */
    private fun navigateToSinglePhotoFullView(photo: Photo) {
        startActivity(Intent(this, SinglePhotoFullViewActivity::class.java).apply {
            putExtra("photo_url", photo.urls?.regular)
        })
    }

    /**
     * Sets up the toolbar as a support actionbar and gets rid of the app title from the toolbar
     */
    private fun initToolbar() {
        setSupportActionBar(binding.singleImageDetailsToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.singleImageDetailsToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getPhotoDetails(photoID: String) {
        val dataService =
            AppModule.providePhotoDataService(retrofit = AppModule.provideRetrofit())
        val photoDetailsCall = dataService.getPhoto(photoID)
        photoDetailsCall.enqueue(object : Callback<Photo?> {
            override fun onResponse(call: Call<Photo?>, response: Response<Photo?>) {
                viewModel.setPhotoDetails(
                    photoId = response.body()?.id,
                    photoURL = response.body()?.urls?.regular,
                    photoDimensions = "${response.body()?.width} x ${response.body()?.height}",
                    photoDescription = response.body()?.description,
                    photoOwnerFullName = response.body()?.user?.name,
                    photoOwnerUsername = response.body()?.user?.username,
                    photoOwnerProfilePic = response.body()?.user?.profileImage?.medium,
                    photoDateCreated = response.body()?.created_at,
                    photoLikeCount = response.body()?.likes,
                    photoLikedByUser = response.body()?.likedByUser,
                    photoCameraMake = response.body()?.exif?.make,
                    photoCameraModel = response.body()?.exif?.model,
                    photoCameraExposure = response.body()?.exif?.exposure_time,
                    photoCameraAperture = response.body()?.exif?.aperture,
                    photoCameraFocalLength = response.body()?.exif?.focal_length,
                    photoCameraISO = response.body()?.exif?.iso,
                    photoLocation = response.body()?.location?.formattedLocation
                )
                viewModel.photo.value = response.body()!!
                response.body()!!.tags?.let { setupPhotoTagsRecyclerView(it) }
            }

            override fun onFailure(call: Call<Photo?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    /**
     * Setup photo tags recycler view
     *
     * @param tags A list of all the tags we're going to add to the recyclerview
     */
    private fun setupPhotoTagsRecyclerView(tags: List<PhotoTags>) {
        val photosTagsAdapter = PhotoTagsRecyclerViewAdapter(photoTags = tags, this)
        binding.apply {
            photoTagsRecyclerView.adapter = photosTagsAdapter
        }
    }

    override fun onPhotoTagClick(photoTag: PhotoTags) {
        Toast.makeText(this, "${photoTag.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onPhotoLikeSuccess() {
        viewModel.photoLikedByUser.value = true
    }

    override fun onPhotoLikeFailure() {
        TODO("Not yet implemented")
    }

    override fun onPhotoUnlikeSuccess() {
        viewModel.photoLikedByUser.value = false
    }

    override fun onPhotoUnlikeFailure() {
        TODO("Not yet implemented")
    }

    override fun onPhotoDownloadSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPhotoDownloadFailure() {
        TODO("Not yet implemented")
    }

}