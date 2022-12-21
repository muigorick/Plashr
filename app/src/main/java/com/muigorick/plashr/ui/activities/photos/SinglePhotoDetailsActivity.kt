package com.muigorick.plashr.ui.activities.photos

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.muigorick.plashr.R
import com.muigorick.plashr.account.AccountManager
import com.muigorick.plashr.actions.PhotoActions
import com.muigorick.plashr.adapters.PhotoTagsRecyclerViewAdapter
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.dataModels.photos.PhotoTags
import com.muigorick.plashr.databinding.ActivitySinglePhotoDetailsBinding
import com.muigorick.plashr.network.AppModule
import com.muigorick.plashr.utils.Utils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SinglePhotoDetailsActivity : AppCompatActivity(),
    PhotoTagsRecyclerViewAdapter.OnPhotoTagClickListener {

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
                viewModel.setPhotoLoaded(loaded = false)
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
            if (viewModel.getPhoto().value != null) {
                Log.i("PHOTO ON CLICK", "photo is not null")
                viewModel.getPhoto().value?.let { photo -> navigateToSinglePhotoFullView(photo = photo) }
            } else {
                Log.i("PHOTO ON CLICK", "photo is null")
            }
        }
        binding.likeButton.setOnClickListener {
            if (AccountManager().isAppAuthorized()) (
                    Toast.makeText(
                        this,
                        "LIKE IMAGE BUTTON CLICKED. USER AUTHORIZED",
                        Toast.LENGTH_SHORT
                    ).show())
            else (
                    Toast.makeText(
                        this,
                        "LIKE IMAGE BUTTON CLICKED. USER NOT AUTHORIZED",
                        Toast.LENGTH_SHORT
                    ).show())
        }

        binding.collectButton.setOnClickListener {
            if (AccountManager().isAppAuthorized()) (
                    Toast.makeText(
                        this,
                        "COLLECT IMAGE BUTTON CLICKED. USER AUTHORIZED",
                        Toast.LENGTH_SHORT
                    ).show())
            else (
                    Toast.makeText(
                        this,
                        "COLLECT IMAGE BUTTON CLICKED. USER NOT AUTHORIZED",
                        Toast.LENGTH_SHORT
                    ).show())
        }

        binding.downloadButton.setOnClickListener {
            /* viewModel.getPhoto().value?.links?.downloadLink?.let { downloadLink ->
                 downloadImage("${viewModel.getPhoto().value!!.user!!.username}-${viewModel.getPhoto().value!!.id}",
                     downloadLink, this
                 )
             }*/
            Toast.makeText(this, "DOWNLOAD BUTTON CLICKED", Toast.LENGTH_SHORT).show()

        }


        binding.viewOnUnsplashButton.setOnClickListener {
            if (viewModel.getPhoto().value?.attributionUrl != null) {
                Utils(this@SinglePhotoDetailsActivity).openWebPage(
                    url = viewModel.getPhoto().value!!.attributionUrl,
                    packageManager = packageManager
                )
                //openWebPage(viewModel.getPhoto().value!!.attributionUrl)
            }
        }

        /*
        * Shares the image
        */
        binding.shareButton.setOnClickListener {
            if (viewModel.getPhoto().value != null) {
                PhotoActions(this, viewModel.getPhoto().value!!).sharePhoto()
            }
        }

    }

    /**
     * Sets all our observers.
     *
     */
    private fun setObservers() {
        viewModel.getPhotoDimensions().observe(this) {
            if (it != null) {
                binding.dimensionsTxt.text = it
            }
        }

        /* viewModel.getPhotoURL().observe(this, { photoURL ->
         if (photoURL != null) {
             Glide.with(this)
                 .load(photoURL)
                 .placeholder(viewModel.getPhoto().value?.photoColor)
                 .into(binding.imageView)
             //viewModel.setPhotoLoaded(loaded = true)
         }
     })*/


        viewModel.getPhotoURL().observe(this) { photoURL ->
            if (photoURL != null) {
                viewModel.getPhotoLoaded().observe(this) { photoIsLoaded ->
                    if (photoIsLoaded == false) {
                        Glide.with(this@SinglePhotoDetailsActivity)
                            .load(photoURL)
                            .placeholder(viewModel.getPhoto().value?.photoColor)
                            .into(binding.imageView)
                        viewModel.setPhotoLoaded(loaded = true)
                    }
                }

            }
        }

        viewModel.getPhotoDescription().observe(this) {
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

        viewModel.getPhotoOwnerFullName().observe(this) {
            if (it != null) {
                binding.userFullNameTxt.text = it
            }
        }

        viewModel.getPhotoOwnerUsername().observe(this) {
            if (it != null) {
                binding.userNameTxt.text = getString(R.string.username, it)
            }
        }

        viewModel.getPhotoOwnerProfilePic().observe(this) {
            if (it != null) {
                Glide.with(this@SinglePhotoDetailsActivity)
                    .load(it)
                    .into(binding.profilePicCircleImageView)
            }
        }

        viewModel.getPhotoDateCreated().observe(this) { date ->
            if (date != null) {
                binding.publishedDateTxt.text = getString(R.string.publish_date, date)
            }
        }

        viewModel.getPhotoLikeCount().observe(this) {
            if (it != null) {
                //  binding..text = it
            }
        }

        //Handle the user liking the photo here
        //TODO update the drawable here. Probably change the fill color or better yet have two drawables where one reflects the color filled version.
        viewModel.getPhotoLikedByUser().observe(this) {
            /* if (it != null) {
             if (it != false)
                 binding.likeImageButton.drawable.setTint(
                     ContextCompat.getColor(
                         this,
                         R.color.likedImageBtnColor
                     )
                 )
             else
                 binding.likeImageButton.drawable.setTint(
                     ContextCompat.getColor(
                         this,
                         R.color.likedImageBtnColor
                     )
                 ) //update this
         }*/
        }

        viewModel.getPhotoCameraMake().observe(this) {
            if (it != null) {
                binding.cameraMakeTxt.text = it
            } else {
                binding.cameraMakeTxt.text = "--"
            }
        }

        viewModel.getPhotoCameraModel().observe(this) {
            if (it != null) {
                binding.cameraModelTxt.text = it
            } else {
                binding.cameraModelTxt.text = "--"
            }
        }

        viewModel.getPhotoCameraExposure().observe(this) {
            if (it != null) {
                binding.photoExposureTxt.text = this.getString(R.string.shutter_speed_count, it)
            } else {
                binding.photoExposureTxt.text = "--"
            }
        }

        viewModel.getPhotoCameraAperture().observe(this) {
            if (it != null) {
                binding.photoApertureTxt.text = this.getString(R.string.aperture_f, it)
            } else {
                binding.photoApertureTxt.text = "--"
            }
        }

        viewModel.getPhotoCameraFocalLength().observe(this) {
            if (it != null) {
                binding.photoFocalLengthTxt.text = this.getString(R.string.focal_length_mm, it)
            } else {
                binding.photoFocalLengthTxt.text = "--"
            }
        }

        viewModel.getPhotoCameraISO().observe(this) {
            if (it != null) {
                binding.photoIsoTxt.text = it.toString()
            } else {
                binding.photoIsoTxt.text = "--"
            }
        }

        viewModel.getPhotoLocation().observe(this) {
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
        /*val intent = Intent(this, SinglePhotoFullViewActivity::class.java).apply {
            putExtra("photo_url", photo.urls?.regular)
        }*/
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
            onBackPressed()
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
                    photoDateCreated = response.body()?.dateUploaded,
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
                viewModel.setPhoto(response.body()!!)
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

    /**
     *  Hides the System UI. In this case, this is the status bar and the navigation bar.
     *  To call the system UI, you just swipe and it auto hides after a while.
     *  This uses an if statement that implements this feature based on the android version.
     */
    private fun hideSystemUI() {
        // For ANDROID R and later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(
                WindowInsets.Type.statusBars().and(WindowInsets.Type.navigationBars())
            )
        } else {
            // For versions under ANDROID R.
            @Suppress("DEPRECATION")
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}

/*
This method can be used to download an image from the internet using a url in Android. This use Android Download Manager to
download the file and added it to the Gallery. Downloaded image will be saved to "Pictures"
Folder in your internal storage
*/
/*private fun downloadImageNew(filename: String, downloadUrlOfImage: String) {
    try {
        val dm = ContextCompat.getSystemService(this@SinglePhotoDetailsActivity, Context.DOWNLOAD_SERVICE) as DownloadManager?
        val downloadUri = Uri.parse(downloadUrlOfImage)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(filename)
            .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + filename + ".jpg"
            )
        dm!!.enqueue(request)
        Toast.makeText( this,"Image download started.", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(this,"Image download failed.", Toast.LENGTH_SHORT).show()
    }
}*/


/*
This method can be used to download an image from the internet using a url in Android. This use Android Download Manager to
download the file and added it to the Gallery. Downloaded image will be saved to "Pictures"
Folder in your internal storage
*/

/*
This method can be used to download an image from the internet using a url in Android. This use Android Download Manager to
download the file and added it to the Gallery. Downloaded image will be saved to "Pictures"
Folder in your internal storage
*/
private fun downloadImage(filename: String, downloadUrlOfImage: String, context: Context) {

    try {

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val downloadUri = Uri.parse(downloadUrlOfImage)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(filename)
            .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + filename + ".jpg"
            )
        dm!!.enqueue(request)
        Toast.makeText(context, "Image download started.", Toast.LENGTH_SHORT).show()
    } catch (e: java.lang.Exception) {
        Toast.makeText(context, "Image download failed.", Toast.LENGTH_SHORT).show()
    }
}







