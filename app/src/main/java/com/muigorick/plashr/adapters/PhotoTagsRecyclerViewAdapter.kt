package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muigorick.plashr.dataModels.photos.PhotoTags
import com.muigorick.plashr.databinding.PhotoTagItemBinding

/**
 * The photo tags recyclerview adapter.
 *
 * @param photoTags A list of the photo tags to be displayed.
 * @param listener A listener that handles the clicks for the tags.
 */
class PhotoTagsRecyclerViewAdapter(
    private val photoTags: List<PhotoTags>,
    private val listener: OnPhotoTagClickListener
) :
    RecyclerView.Adapter<PhotoTagsRecyclerViewAdapter.PhotoTagsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoTagsViewHolder {
        val binding =
            PhotoTagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoTagsViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: PhotoTagsViewHolder, position: Int) {
        val currentItem = photoTags[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return photoTags.size
    }

    /**
     * ViewHolder for our tags.
     */
    inner class PhotoTagsViewHolder(private val binding: PhotoTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tagTitleChip.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = photoTags[position]
                    listener.onPhotoTagClick(photoTag = item)
                }
            }
        }

        fun bind(photoTags: PhotoTags) {
            binding.tagTitleChip.text = photoTags.title
        }
    }

    /**
     * This is the tag click listener. We have one function:
     *
     * 1. [onPhotoTagClick] is used when the user clicks on tag.
     */
    interface OnPhotoTagClickListener {
        /**
         * Handles the click when the tag is clicked.
         *
         * @param photoTag used to identify which tag has been clicked.
         */
        fun onPhotoTagClick(photoTag: PhotoTags)
    }
}