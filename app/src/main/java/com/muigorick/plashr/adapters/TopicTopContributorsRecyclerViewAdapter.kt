package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.topics.Topic
import com.muigorick.plashr.databinding.TopContributorsListItemBinding
/**
 * Topic contributors recyclerview adapter.
 *
 * We use this to display the topic top contributors.
 *
 * @param topContributors This is a list of the top contributors we are going to display on the recyclerview.
 * @param listener  A click listener used to handle the topic's top contributor's clicks.
 *
 */
class TopicTopContributorsRecyclerViewAdapter(
    private val topContributors: List<Topic.TopContributors>,
    private val listener: OnOwnerClickListener
) : RecyclerView.Adapter<TopicTopContributorsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TopContributorsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = topContributors[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return topContributors.size
    }

    /**
     * ViewHolder for our top contributors.
     */
    inner class ViewHolder(private val binding: TopContributorsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.contributorImageView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = topContributors[position]
                    listener.onContributorClick(topContributor = item)
                }
            }
        }

        fun bind(topContributors: Topic.TopContributors) {
            Glide.with(itemView)
                .load(topContributors.profilePhotos.mediumProfileImage)
                .into(binding.contributorImageView)
        }
    }

    /**
     * This is the top contributor click listener. We have one function:
     *
     * 1. [onContributorClick] is used when the user clicks on top contributor's profile picture.
     */
    interface OnOwnerClickListener {
        /**
         * Handles the click when the top contributor's profile picture is clicked.
         *
         * @param topContributor used to identify which top contributor's profile picture has been clicked.
         */
        fun onContributorClick(topContributor: Topic.TopContributors)
    }
}