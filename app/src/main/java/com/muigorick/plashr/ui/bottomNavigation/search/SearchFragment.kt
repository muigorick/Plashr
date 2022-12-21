package com.muigorick.plashr.ui.bottomNavigation.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muigorick.plashr.R
import com.muigorick.plashr.databinding.FragmentCollectionsBinding
import com.muigorick.plashr.databinding.FragmentProfileBinding
import com.muigorick.plashr.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * TODO Handle the filter option for photos. 1. *Maybe implement both settings and filter*. Given this implementation, we'll have to look more into not allowing the user to choose their own viewtype for the photos since we'll have to reload a lot.
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchFragmentViewModel::class.java)
    }

    // Destroys the ViewBinding.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}