package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muigorick.plashr.dataModels.topics.Topic
import com.muigorick.plashr.databinding.TopContributorsListItemBinding

class TopicOwnersRecyclerViewAdapter(
    private val owners: List<Topic.Owners>,
    private val listener: OnOwnerClickListener
) :
    RecyclerView.Adapter<TopicOwnersRecyclerViewAdapter.CollectionOwnersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionOwnersViewHolder {
        val binding =
           TopContributorsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionOwnersViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: CollectionOwnersViewHolder, position: Int) {
        val currentItem = owners[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return owners.size
    }

    /**
     * ViewHolder for our owners.
     */
    inner class CollectionOwnersViewHolder(private val binding: TopContributorsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.contributorImageView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = owners[position]
                    listener.onOwnerClick(owner = item)
                }
            }
        }

        fun bind(owner: Topic.Owners) {
            Glide.with(itemView)
                .load(owner.profilePhotos.mediumProfileImage)
                .into(binding.contributorImageView)
        }
    }

    /**
     * This is the owner click listener. We have one function:
     *
     * 1. [onOwnerClick] is used when the user clicks on owner's profile picture.
     */
    interface OnOwnerClickListener {
        /**
         * Handles the click when the owner's profile picture is clicked.
         *
         * @param owner used to identify which profile picture has been clicked.
         */
        fun onOwnerClick(owner: Topic.Owners)
    }


}
