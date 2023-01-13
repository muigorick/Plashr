package com.muigorick.plashr.ui.activities.collections

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.muigorick.plashr.R
import com.muigorick.plashr.actions.CollectionActions
import com.muigorick.plashr.adapters.PhotoRecyclerViewAdapter
import com.muigorick.plashr.dataModels.collections.Collection
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.databinding.ActivitySingleCollectionDetailsBinding
import com.muigorick.plashr.ui.activities.photos.SinglePhotoDetailsActivity
import com.muigorick.plashr.ui.activities.settings.SettingsViewModel
import com.muigorick.plashr.utils.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import kotlin.math.abs

@AndroidEntryPoint
class SingleCollectionDetailsActivity : AppCompatActivity(),
    PhotoRecyclerViewAdapter.OnPhotoClickListener {

    private lateinit var binding: ActivitySingleCollectionDetailsBinding
    private val viewModel: SingleCollectionDetailsActivityViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleCollectionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
        handleCollectionIntent(intent = intent)
        setCollectionObservers()
    }

    /**
     * Handles the incoming intent that contains the photo bundle.
     *
     * @param intent Intent the photo bundle will be extracted from.
     */
    @Suppress("DEPRECATION")
    private fun handleCollectionIntent(intent: Intent) {
        when {
            (intent.extras != null) -> {
                val collection = when {
                    Build.VERSION.SDK_INT >= 33 -> {
                        intent.getParcelableExtra("collection", Collection::class.java)

                    }
                    else -> {
                        intent.getParcelableExtra("collection")
                    }
                }
                when {
                    collection != null -> {
                        viewModel.setCollectionDetails(
                            collectionId = collection.id,
                            collectionTitle = collection.title,
                            collectionDescription = collection.description,
                            collectionPublishedDate = collection.published_at,
                            collectionTotalPhotos = collection.totalPhotos,
                            collectionIsPrivate = collection.private,
                            collectionCoverPhoto = collection.coverPhoto,
                            collectionUser = collection.user,
                        )

                        viewModel.setCollection(collection = collection)
                    }
                    else -> {
                        // Handle the deeplink here. Below is how the address if formatted.
                        // https://unsplash.com/collections/1118919/coffee-culture-%E2%98%95%EF%B8%8F
                        // Use the ID as we can fetch it from the data portion of the address.

                        //getCollectionDetails(collectionID = Utils(context = this@SingleCollectionDetailsActivity).handleDeeplinkIntent(intent = intent)!!.pathSegments[1])

                    }
                }
            }
        }
    }

    /**
     * Sets up the toolbar as a support actionbar and adds a navigation back button.
     * It also adds an appBar Listener for when we want to change the title.
     */
    private fun initToolbar() {
        setSupportActionBar(binding.singleCollectionActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.singleCollectionActivityToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                // Collapsed
                binding.singleCollectionActivityToolbar.title = viewModel.getCollectionTitle().value
            } else {
                // Expanded
                binding.singleCollectionActivityToolbar.title = null
                binding.collapsingToolbar.title = null
            }
        }
    }

    /**
     * Sets up our menu for the activity.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.single_collection_toolbar_menu, menu)
        return true
    }

    /**
     * Handles the click actions for our toolbar menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shareCollection -> {
                Log.i("MENU", "onOptionsItemSelected: Share Collection Clicked!")
                if (viewModel.getCollectionID().value != null) {
                    CollectionActions(
                        this, collection = viewModel.getCollection().value!!
                    ).shareCollection()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Sets up the collection observers.
     */
    private fun setCollectionObservers() {
        viewModel.getCollectionID().observe(this) {

        }

        viewModel.getCollectionTitle().observe(this) { title ->
            binding.collectionTitleTxt.text = title
        }

        viewModel.getCollectionDescription().observe(this) { description ->
            if (description == null) binding.collectionDescriptionTxt.visibility = View.GONE
            else binding.collectionDescriptionTxt.text = description
        }

        viewModel.getCollectionPublishedDate().observe(this) {

        }

        viewModel.getCollectionTotalPhotos().observe(this) { totalPhotos ->
            binding.photoCountTxt.text = resources.getQuantityString(
                R.plurals.collection_photo_count,
                totalPhotos,
                NumberFormat.getInstance().format(totalPhotos)
            )
        }

        viewModel.getCollectionIsPrivate().observe(this) {

        }

        viewModel.getCollectionCoverPhoto().observe(this) {

        }

        viewModel.getCollectionUser().observe(this) { user ->
            binding.curatedByTxt.text = resources.getString(R.string.curated_by_username, user.name)
            Glide.with(this).load(user.profileImage!!.medium)
                .into(binding.collectionCuratorProfilePicture)
        }
    }

    private fun initRecyclerView() {
        val photosAdapter = PhotoRecyclerViewAdapter(this, SharedPreferences(this))
        binding.apply {
            collectionPhotosRecyclerView.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = photosAdapter
            }

            settingsViewModel.readImageLayoutFromDataStore.observe(
                this@SingleCollectionDetailsActivity
            ) { imageLayout ->
                when (imageLayout) {
                    "Grid" -> collectionPhotosRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    else -> collectionPhotosRecyclerView.layoutManager =
                        LinearLayoutManager(this@SingleCollectionDetailsActivity)
                }
            }
        }

        viewModel.collectionPhotos.observe(this) { photos ->
            binding.collectionPhotosRecyclerView.scrollToPosition(0)
            photosAdapter.submitData(lifecycle = lifecycle, pagingData = photos)
        }
    }

    override fun onPhotoClick(photo: Photo) {
        Log.i("Collection Photo", "onPhotoClick: $photo ")
        val intent = Intent(
            this@SingleCollectionDetailsActivity, SinglePhotoDetailsActivity::class.java
        ).apply {
            putExtra("photo", photo)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(this@SingleCollectionDetailsActivity)
                .toBundle()
        )
    }

    override fun onPhotoOwnerClick(photo: Photo) {
        Toast.makeText(this, "OWNER CLICKED", Toast.LENGTH_SHORT).show()
    }
}