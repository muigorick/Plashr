package com.muigorick.plashr.ui.bottomNavigation.home.topics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.muigorick.plashr.adapters.LoadStateAdapter
import com.muigorick.plashr.adapters.TopicsRecyclerViewAdapter
import com.muigorick.plashr.dataModels.topics.Topic
import com.muigorick.plashr.databinding.FragmentTopicsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicsFragment : Fragment(), TopicsRecyclerViewAdapter.OnTopicClickListener {
    val TAG: String = "TopicsFragment"

    companion object {
        fun newInstance() = TopicsFragment()
    }

    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!
    private val topicsFragmentViewModel: TopicsFragmentViewModel by activityViewModels()
    private lateinit var topicsAdapter: TopicsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*
         *  TODO Add a load state listener with a progress bar to indicate loading status.
                Definitely design it first since we're dealing with a horizontal recyclerview.*/

        topicsAdapter = TopicsRecyclerViewAdapter(this)
        setupTopics()
    }



    /**
     * Sets up the topics and initializes the respective recyclerview to be viewed on the Home Fragment.
     *
     */
    private fun setupTopics() {
        binding.apply {
            topicsRecyclerView.itemAnimator = null
            topicsRecyclerView.adapter = topicsAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { topicsAdapter.retry() },
                footer = LoadStateAdapter { topicsAdapter.retry() }
            )
            topicsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        topicsFragmentViewModel.topics.observe(viewLifecycleOwner) { topics ->
            topicsAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, pagingData = topics)
        }
    }

    /**
     * Sets up the topics to be viewed on the Home Fragment
     *
     */
    /*private fun setupTopics() {
        binding.apply {
            topicsRecyclerView.itemAnimator = null
            topicsRecyclerView.adapter = topicsAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { topicsAdapter.retry() },
                footer = LoadStateAdapter { topicsAdapter.retry() }
            )
            topicsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        topicsAdapter.addLoadStateListener { loadState ->
            binding.apply {
                *//*progressbar.isVisible = loadState.source.refresh is LoadState.Loading
                homePhotosRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                loadingErrorLayout.root.isVisible = loadState.source.refresh is LoadState.Error*//*

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    topicsAdapter.itemCount < 1
                ) {
                    topicsRecyclerView.isVisible = false
                    //resultsEmpty.root.isVisible = true
                } else {
                    //resultsEmpty.root.isVisible = false
                }
            }
        }


        topicsFragmentViewModel.topics.observe(viewLifecycleOwner) { topics ->
            topicsAdapter.submitData(lifecycle = viewLifecycleOwner.lifecycle, pagingData = topics)
        }

    }*/

    /**
     * Navigates us to the topic who's details we want to view
     *
     * @param topic
     *
     */
    override fun onTopicClick(topic: Topic) {
        /*val actions = TopicsFragmentDirections.actionTopicsToSingleTopicActivity().setTopic(topic)
        findNavController().navigate(actions)*/
    }
    /**
     * Show's a bottom sheet that has the topic's preview photos and a few topic details.
     * TODO Design and implement the bottom sheet.
     * @param topic Topic a user wants to see more details of.
     */
    override fun onTopicLongClick(topic: Topic) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}