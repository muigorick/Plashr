package com.muigorick.plashr.ui.bottomNavigation.home.editorialPhotos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.muigorick.plashr.adapters.LoadStateAdapter
import com.muigorick.plashr.adapters.PhotoRecyclerViewAdapter
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.databinding.FragmentEditorialPhotosBinding
import com.muigorick.plashr.ui.activities.photos.SinglePhotoDetailsActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class EditorialPhotosFragment : Fragment(),PhotoRecyclerViewAdapter.OnPhotoClickListener {

    companion object {
        fun newInstance() =EditorialPhotosFragment()
    }

    private var _binding: FragmentEditorialPhotosBinding? = null
    private val binding get() = _binding!!
    private val editorialPhotosFragmentViewModel: EditorialPhotosFragmentViewModel by activityViewModels()
    private lateinit var editorialPhotosAdapter: PhotoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentEditorialPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhotos()
    }

    private fun setupPhotos(){
        editorialPhotosAdapter = PhotoRecyclerViewAdapter(this,null)

        binding.editorialPhotosRecyclerView.apply {
           itemAnimator = null
            adapter = editorialPhotosAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { editorialPhotosAdapter.retry() },
                footer = LoadStateAdapter { editorialPhotosAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())

        }

        editorialPhotosAdapter.addLoadStateListener { loadState ->
            binding.apply {
                //progressbar.isVisible = loadState.source.refresh is LoadState.Loading
                editorialPhotosRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                // loadingErrorLayout.root.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    editorialPhotosAdapter.itemCount < 1
                ) {
                    editorialPhotosRecyclerView.isVisible = false
                    // resultsEmpty.root.isVisible = true
                } else {
                    // resultsEmpty.root.isVisible = false
                }
            }
        }

        lifecycleScope.launch {
            editorialPhotosFragmentViewModel.photos.collectLatest { pagingData ->
                editorialPhotosAdapter.submitData(pagingData = pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPhotoClick(photo: Photo) {
        val photoIntent = Intent(requireActivity(), SinglePhotoDetailsActivity::class.java)
        photoIntent.putExtra("photo", photo)
        startActivity(photoIntent)
    }

    override fun onPhotoOwnerClick(photo: Photo) {
        Toast.makeText(requireContext(), "OWNER CLICKED", Toast.LENGTH_SHORT).show()
    }
}