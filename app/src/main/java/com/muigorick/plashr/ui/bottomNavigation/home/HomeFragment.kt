package com.muigorick.plashr.ui.bottomNavigation.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

@AndroidEntryPoint
class HomeFragment : Fragment(),
    PhotoRecyclerViewAdapter.OnPhotoClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val editorialPhotosFragmentViewModel: EditorialPhotosFragmentViewModel by viewModels()
    private lateinit var settingsViewModel: SettingsViewModel

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
        photosAdapter = PhotoRecyclerViewAdapter(this, SharedPreferences(requireActivity()))
        settingsViewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        setupPhotos()
    }

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