package com.muigorick.plashr.ui.bottomNavigation.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.muigorick.plashr.R
import com.muigorick.plashr.adapters.LoadStateAdapter
import com.muigorick.plashr.adapters.PhotoRecyclerViewAdapter
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.databinding.FragmentHomeBinding
import com.muigorick.plashr.ui.activities.photos.SinglePhotoDetailsActivity
import com.muigorick.plashr.ui.activities.settings.SettingsActivity
import com.muigorick.plashr.ui.activities.settings.SettingsViewModel
import com.muigorick.plashr.ui.bottomNavigation.home.editorialPhotos.EditorialPhotosFragmentViewModel
import com.muigorick.plashr.utils.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
TODO Implement nested scrollview for the 2 categories: Topics and photos. PS: Look into incorporating the filter
     function for the photos based on what the user wants to see.
     1. If it causes bugs in the way the recyclerview is handled especially when it comes to scrolling, please find a way to nest the two
        different layouts in one rather than having a recyclerview to hold both. --- Solution 1 (FAILED)=> Setting different viewtypes doesn't work because of paging.
*/

@AndroidEntryPoint
class HomeFragment : Fragment(), /*TopicsRecyclerViewAdapter.OnTopicClickListener,*/
    PhotoRecyclerViewAdapter.OnPhotoClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var layoutManager: LayoutManager? = null
    // private var columnCount:Int?=null

    //private val topicsFragmentViewModel: TopicsFragmentViewModel by viewModels()
    private val editorialPhotosFragmentViewModel: EditorialPhotosFragmentViewModel by viewModels()
    private lateinit var settingsViewModel: SettingsViewModel

    // private lateinit var topicsAdapter: TopicsRecyclerViewAdapter
    private lateinit var photosAdapter: PhotoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.home_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        startActivity(Intent(requireActivity(), SettingsActivity::class.java))
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  topicsAdapter = TopicsRecyclerViewAdapter(this)
        photosAdapter = PhotoRecyclerViewAdapter(this, SharedPreferences(requireActivity()))
        settingsViewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        // setupTopics()
        setupPhotos()
    }

    /*  */
    /**
     * Sets up the topics and initializes the respective recyclerview to be viewed on the Home Fragment.
     *
     *//*
    private fun setupTopics() {

        binding.apply {
            topicsRecyclerView.itemAnimator = null
            topicsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            topicsRecyclerView.adapter = topicsAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { topicsAdapter.retry() },
                footer = LoadStateAdapter { topicsAdapter.retry() }
            )
        }

        topicsFragmentViewModel.topics.observe(viewLifecycleOwner) { topics ->
            topicsAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, pagingData = topics)
        }
    }*/

    /**
     * Sets up the photos and initializes the respective recyclerview to be viewed on the Home Fragment.
     *
     * TODO Add the error layouts
     */
    private fun setupPhotos() {
        binding.apply {
            homePhotosRecyclerView.itemAnimator = null
            homePhotosRecyclerView.adapter = photosAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { photosAdapter.retry() },
                footer = LoadStateAdapter { photosAdapter.retry() }
            )

            settingsViewModel.readImageLayoutFromDataStore.observe(
                requireActivity()
            ) { imageLayout ->
                when (imageLayout) {
                    "Grid" ->
                        homePhotosRecyclerView.layoutManager =
                            StaggeredGridLayoutManager(
                                resources.getInteger(R.integer.staggered_grid_column_count),
                                StaggeredGridLayoutManager.VERTICAL
                            )
                    "Cards" ->
                        homePhotosRecyclerView.layoutManager =
                            GridLayoutManager(
                                context,
                                resources.getInteger(R.integer.column_count)
                            )

                    else -> homePhotosRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())

                }
            }

            /* homePhotosRecyclerView.layoutManager =layoutManager*/

            loadingErrorLayout.retryTxtBtn.setOnClickListener {
                photosAdapter.retry()
            }
        }

        lifecycleScope.launch {
            editorialPhotosFragmentViewModel.photos.collectLatest { pagingData ->
                photosAdapter.submitData(pagingData = pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* */
    /**
     * Navigates us to the [com.muigorick.plashr.ui.activities.topics.SingleTopicDetailsActivity] where the user can view more details about the topic they've chosen.
     * @param topic Topic a user wants to see more details of.
     *//*
    override fun onTopicClick(topic: Topic) {
        val bundle = bundleOf("topic" to topic)
        findNavController().navigate(R.id.action_HomeFragment_to_SingleTopicDetailsActivity, bundle)
    }*/
/*

    */

    /**
     * Show's a bottom sheet that has the topics preview photos and a few topic details.
     * TODO Design and implement the bottom sheet.
     * @param topic Topic a user wants to see more details of.
     *//*

    override fun onTopicLongClick(topic: Topic) {
        Toast.makeText(requireContext(), "TOPIC CLICKED : ON LONG CLICK", Toast.LENGTH_SHORT).show()
    }
*/

    /**
     * Navigates us to the [SinglePhotoDetailsActivity] where the user can view more details about the photo.
     * @param photo Photo a user wants to see more details of.
     */
    override fun onPhotoClick(photo: Photo) {
        val bundle = bundleOf("photo" to photo)
        findNavController().navigate(R.id.action_HomeFragment_to_SinglePhotoDetailsActivity, bundle)
    }

    /**
     * Navigates us to the [] where the user can view the photo owner's profile..
     * @param photo Photo a user wants to see the owner's profile.
     */
    override fun onPhotoOwnerClick(photo: Photo) {
        Toast.makeText(requireContext(), "OWNER CLICKED", Toast.LENGTH_SHORT).show()
    }
}