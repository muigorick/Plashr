package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.collections.Collection
import com.muigorick.plashr.databinding.CollectionCardItemBinding

/**
 * This is our collections recyclerview adapter.
 * It is reused whenever we fetch paginated collections that are meant to be displayed in a recyclerview
 *
 * @param listener  The click listener used to handle the collection click listeners.
 */
class CollectionsRecyclerViewAdapter(private val listener: OnCollectionClickListener) :
    PagingDataAdapter<Collection, CollectionsRecyclerViewAdapter.CollectionViewHolder>(
        COLLECTION_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            CollectionCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CollectionViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        getItem(position = position)?.let { holder.bind(it) }
    }

    inner class CollectionViewHolder(private val binding: CollectionCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(bindingAdapterPosition)
                    if (item != null) {
                        listener.onCollectionClick(collection = item)
                    }
                }
            }
            binding.collectionCuratorProfilePicImageView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(bindingAdapterPosition)
                    if (item != null) {
                        listener.onCollectionOwnerClick(collection = item)
                    }
                }
            }
        }

        fun bind(collection: Collection) {
            binding.collectionNameTxt.text = collection.title
            binding.collectionCuratorFullNameTxt.text = collection.user?.name
            Glide.with(itemView)
                .load(collection.user?.profileImage!!.medium)
                .into(binding.collectionCuratorProfilePicImageView)
            Glide.with(itemView)
                .load(collection.coverPhoto!!.urls.regular)
                .into(binding.collectionCoverImageView)
            binding.collectionPhotoCountTxt.text =
                itemView.context.getString(
                    R.string.photo_count,
                    collection.totalPhotosCountFormatted
                ) // TODO Change the photo count to reflect singularity and plurals
        }
    }

    /**
     * This is the collection click listener interface. We have two functions:
     * 1. [onCollectionClick] is used when the user clicks on elements that navigate them to view the collection's details.
     * 2. [onCollectionOwnerClick] is used when the user clicks on elements that navigate them to the collection's owner profile.
     */
    interface OnCollectionClickListener {
        /**
         * Handles the click when the whole collection card is clicked
         */
        fun onCollectionClick(collection: Collection)
        /**
         * Overrides the function above when the user clicks on the user profile pic on the collection card
         */
        fun onCollectionOwnerClick(collection: Collection)
    }

    /**
     * Compares the collections so as not to show them multiple times in the recyclerview
     */
    companion object {
        private val COLLECTION_COMPARATOR = object : DiffUtil.ItemCallback<Collection>() {
            override fun areItemsTheSame(oldItem: Collection, newItem: Collection) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Collection, newItem: Collection) =
                oldItem == newItem
        }
    }
}