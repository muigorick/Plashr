package com.muigorick.plashr.ui.bottomNavigation.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.muigorick.plashr.R
import com.muigorick.plashr.adapters.CollectionsRecyclerViewAdapter
import com.muigorick.plashr.adapters.LoadStateAdapter
import com.muigorick.plashr.dataModels.collections.Collection
import com.muigorick.plashr.databinding.FragmentCollectionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment : Fragment(), CollectionsRecyclerViewAdapter.OnCollectionClickListener {

    companion object {
        fun newInstance() = CollectionsFragment()
    }

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CollectionsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCollections()
    }

    /**
     * Sets up the collections to be viewed on the Collection Fragment
     */
    private fun setupCollections() {
        val collectionsRecyclerViewAdapter = CollectionsRecyclerViewAdapter(this)
        binding.apply {
            collectionsRecyclerview.itemAnimator = null
            collectionsRecyclerview.adapter =
                collectionsRecyclerViewAdapter.withLoadStateHeaderAndFooter(
                    header = LoadStateAdapter { collectionsRecyclerViewAdapter.retry() },
                    footer = LoadStateAdapter { collectionsRecyclerViewAdapter.retry() }
                )
            collectionsRecyclerview.layoutManager = LinearLayoutManager(context)
        }

        viewModel.collections.observe(viewLifecycleOwner) { collections ->
            collectionsRecyclerViewAdapter.submitData(
                lifecycle = viewLifecycleOwner.lifecycle,
                pagingData = collections
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCollectionClick(collection: Collection) {
        val bundle = bundleOf("collection" to collection)
        findNavController().navigate(R.id.action_collections_to_collectionDetailsActivity, bundle)
    }

    override fun onCollectionOwnerClick(collection: Collection) {
       // TODO To be added later by showing a user's profile via a bottom sheet.
    }
}