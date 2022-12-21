package com.muigorick.plashr.ui.activities.topics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.muigorick.plashr.R
import com.muigorick.plashr.actions.TopicActions
import com.muigorick.plashr.adapters.PhotoRecyclerViewAdapter
import com.muigorick.plashr.adapters.TopicOwnersRecyclerViewAdapter
import com.muigorick.plashr.adapters.TopicTopContributorsRecyclerViewAdapter
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.dataModels.topics.Topic
import com.muigorick.plashr.databinding.ActivitySingleTopicBinding
import com.muigorick.plashr.network.AppModule
import com.muigorick.plashr.ui.activities.photos.SinglePhotoDetailsActivity
import com.muigorick.plashr.ui.activities.settings.SettingsViewModel
import com.muigorick.plashr.utils.SharedPreferences
import com.muigorick.plashr.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import kotlin.math.abs

@AndroidEntryPoint
class SingleTopicDetailsActivity : AppCompatActivity(),
    PhotoRecyclerViewAdapter.OnPhotoClickListener,
    TopicOwnersRecyclerViewAdapter.OnOwnerClickListener,
    TopicTopContributorsRecyclerViewAdapter.OnOwnerClickListener {
    private lateinit var binding: ActivitySingleTopicBinding
    private val viewModel: SingleTopicActivityViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var photosAdapter: PhotoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        photosAdapter = PhotoRecyclerViewAdapter(this, SharedPreferences(this))

        initToolbar()
        initRecyclerView()

        if (intent?.extras != null) {
            Log.i("SINGLE TOPIC DETAILS", "onNewIntent: ${intent.dataString}")

            if (intent.getParcelableExtra<Topic>("topic") != null) {
                val topic = intent.getParcelableExtra<Topic>("topic")
                topic?.let {
                    viewModel.topic.value = topic
                    viewModel.topicId.value = topic.id
                    getTopicDetails(topicID = topic.id)
                    Glide.with(this)
                        .load(topic.coverPhoto.urls.regular)
                        .placeholder(topic.coverPhoto.coverPhotoColor)
                        .into(binding.topicCoverImage)
                }
            }
        } else {
            getTopicDetails(
                topicID = Utils(context = this@SingleTopicDetailsActivity).handleDeeplinkIntent(
                    intent = intent
                )!!.pathSegments[1]
            )
        }

        setObservers()
    }

    /**
     * Initializes the recycler view for the topic's photos.
     *
     * It sets the adapter, layout manager and observes the photos that are submitted to the adapter.
     *
     */
    private fun initRecyclerView() {
        binding.apply {
            topicPhotosRecyclerView.setHasFixedSize(true)
            topicPhotosRecyclerView.itemAnimator = null
            topicPhotosRecyclerView.adapter = photosAdapter
        }

        settingsViewModel.readImageLayoutFromDataStore.observe(
            this@SingleTopicDetailsActivity
        ) { imageLayout ->
            when (imageLayout) {
                "Grid" -> binding.topicPhotosRecyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                else -> binding.topicPhotosRecyclerView.layoutManager =
                    LinearLayoutManager(this@SingleTopicDetailsActivity)
            }
        }

        viewModel.photos.observe(this) { photos ->
            binding.topicPhotosRecyclerView.scrollToPosition(0)
            photosAdapter.submitData(lifecycle = lifecycle, pagingData = photos)
        }
    }

    /**
     * Sets up the photo tags recycler view
     *
     * @param owners A list of all the tags we're going to add to the recyclerview
     */
    private fun setupTopicOwnersRecyclerView(owners: List<Topic.Owners>) {
        val topicOwnersRecyclerViewAdapter = TopicOwnersRecyclerViewAdapter(owners = owners, this)
        binding.apply {
            ownersRecyclerView.adapter = topicOwnersRecyclerViewAdapter
        }
    }

    /**
     * Setup photo tags recycler view
     *
     * @param topicContributor A list of all the tags we're going to add to the recyclerview
     */
    private fun setupTopicTopContributorsRecyclerView(topicContributor: List<Topic.TopContributors>) {
        val topicTopContributorsRecyclerViewAdapter =
            TopicTopContributorsRecyclerViewAdapter(topContributors = topicContributor, this)
        binding.apply {
            topContributorsRecyclerView.adapter = topicTopContributorsRecyclerViewAdapter
        }
    }

    /**
     * Sets all our observers.
     *
     */
    private fun setObservers() {
        viewModel.topicId.observe(this) { topicID ->
            // getTopicDetails(topicID = topicID)
            // TODO Don't use this for the time being since the calls made are looped. Find a way to store just one value and observe that.
            Log.i("SINGLE TOPIC ID", "setObservers: $topicID")
        }
        viewModel.topic.observe(this) { topic ->
            binding.topicTitleTxt.text = topic.title
            binding.topicDescriptionTxt.text = topic.description
            binding.totalTopicPhotosTxt.text =
                getString(
                    R.string.topic_count,
                    NumberFormat.getInstance().format(topic.totalPhotos)
                )
            topic.owners?.let { setupTopicOwnersRecyclerView(it) }
            topic.topContributors?.let { setupTopicTopContributorsRecyclerView(it) }
            Glide.with(this)
                .load(topic.coverPhoto.urls.regular)
                .placeholder(topic.coverPhoto.coverPhotoColor)
                .into(binding.topicCoverImage)
        }
    }

    /**
     * Initializes the toolbar as a support action bar, sets a margin to the toolbar based on the device's status bar height.
     *
     * It also adds an OffsetChangedListener to the appbarLayout so as to declare the correct  color to the status and the navigation bars
     *
     */
    private fun initToolbar() {
        setSupportActionBar(binding.singleTopicActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.singleTopicActivityToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                // Collapsed
                binding.singleTopicActivityToolbar.title =
                    viewModel.topic.value?.title
            } else {
                // Expanded
                binding.singleTopicActivityToolbar.title = null
                binding.collapsingToolbar.title = null
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.single_topic_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shareTopic -> {
                if (viewModel.topic.value != null) {
                    TopicActions(this, viewModel.topic.value!!).shareTopic()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Gets the topic details.
     *
     * @param topicID Topic ID we intend to fetch details of.
     */
    private fun getTopicDetails(topicID: String) {
        // TODO("Not yet implemented")
        val dataService =
            AppModule.provideTopicsDataService(retrofit = AppModule.provideRetrofit())

        dataService.getTopic(id = topicID).enqueue(object : Callback<Topic> {
            override fun onResponse(call: Call<Topic>, response: Response<Topic>) {
                response.body()?.let {
                    viewModel.setTopic(it)
                    // response.body()?.owners?.let { setupTopicOwnersRecyclerView(it) }
                    Log.i(
                        "TOP CONTRIBUTORS",
                        "Top contributors: ${response.body()!!.topContributors}"
                    )
                    // response.body()!!.topContributors?.let { setupTopicTopContributorsRecyclerView(it) }
                }
            }

            override fun onFailure(call: Call<Topic>, t: Throwable) {
                // TODO("Not yet implemented")
            }
        })
    }

    /**
     * Navigates us to the [SinglePhotoDetailsActivity] where the user can view more details about the photo.
     * @param photo Photo a user wants to see more details of.
     */
    override fun onPhotoClick(photo: Photo) {
        Log.i("Collection Photo", "onPhotoClick: $photo ")
        /*val intent = Intent(this@SingleCollectionDetailsActivity, SinglePhotoDetailsActivity::class.java)
        intent.putExtra("photo_model", photo as Parcelable)
        this.startActivity(intent)*/
        val intent = Intent(
            this@SingleTopicDetailsActivity,
            SinglePhotoDetailsActivity::class.java
        ).apply {
            putExtra("photo", photo)
        }
        startActivity(
            intent
        )
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

    /**
     * Navigates us to the [] where the user can view the photo owner's profile..
     * @param photo Photo a user wants to see the owner's profile.
     */
    override fun onPhotoOwnerClick(photo: Photo) {
        Toast.makeText(this, "OWNER CLICKED", Toast.LENGTH_SHORT).show()
    }


    override fun onOwnerClick(owner: Topic.Owners) {
        Toast.makeText(this, "TOPIC OWNER CLICKED: ${owner.username}", Toast.LENGTH_SHORT).show()
    }

    override fun onContributorClick(topContributor: Topic.TopContributors) {
        Toast.makeText(
            this,
            "TOPIC CONTRIBUTOR CLICKED: ${topContributor.username}",
            Toast.LENGTH_SHORT
        ).show()
    }
}