package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.databinding.PhotoCardItemBinding
import com.muigorick.plashr.databinding.PhotoListItemBinding
import com.muigorick.plashr.databinding.PhotoStaggeredGridItemBinding
import com.muigorick.plashr.utils.SharedPreferences

/**
 * Photos recyclerview adapter.
 *
 * It is reused whenever we fetch paginated photos that are meant to be displayed in a recyclerview.
 *
 * @param listener  The click listener used to handle the click listeners.
 * @param sharedPreferences Used to fetch the user's desired image layout.
 */

class PhotoRecyclerViewAdapter(
    private val listener: OnPhotoClickListener,
    sharedPreferences: SharedPreferences?
) : PagingDataAdapter<Photo, RecyclerView.ViewHolder>(PHOTO_COMPARATOR) {

    private val imageLayout = sharedPreferences?.imageLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.photo_list_item -> PhotoListViewHolder(
                PhotoListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.photo_card_item -> PhotoCardViewViewHolder(
                PhotoCardItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.photo_staggered_grid_item -> PhotoStaggeredGridViewHolder(
                PhotoStaggeredGridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position = position)
        when (getItemViewType(position)) {
            R.layout.photo_list_item -> (holder as PhotoListViewHolder).bind(currentItem!!)
            R.layout.photo_card_item -> (holder as PhotoCardViewViewHolder).bind(currentItem!!)
            R.layout.photo_staggered_grid_item -> (holder as PhotoStaggeredGridViewHolder).bind(
                currentItem!!
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (imageLayout) {
            "List" -> R.layout.photo_list_item
            "Cards" -> R.layout.photo_card_item
            "Grid" -> R.layout.photo_staggered_grid_item
            else -> R.layout.photo_list_item
        }
    }

    /**
     * ViewHolder for our photo list.
     */
    inner class PhotoListViewHolder(private val binding: PhotoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onPhotoClick(photo = item)
                    }
                }
            }
        }

        fun bind(photo: Photo) {
            binding.photoOwnerTxt.text = photo.user?.username
            Glide.with(itemView)
                .load(photo.urls?.regular)
                .placeholder(photo.photoColor)
                .into(binding.photoListImageView)

        }
    }

    /**
     * ViewHolder for our photo card view.
     */
    inner class PhotoCardViewViewHolder(private val binding: PhotoCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // TODO Implement better onClickListeners for the other components.
        init {

            fun isItemNull(): Boolean {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        return false
                    }
                }
                return true
            }

            binding.photoOwnerProfilePic.setOnClickListener {
                if (!isItemNull()) {
                    getItem(bindingAdapterPosition)?.let { it1 -> listener.onPhotoOwnerClick(photo = it1) }
                }
            }

            binding.photoOwnerTxt.setOnClickListener {
                if (!isItemNull()) {
                    getItem(bindingAdapterPosition)?.let { it1 -> listener.onPhotoOwnerClick(photo = it1) }
                }
            }
            binding.photoOwnerUsernameTxt.setOnClickListener {
                if (!isItemNull()) {
                    getItem(bindingAdapterPosition)?.let { it1 -> listener.onPhotoOwnerClick(photo = it1) }
                }
            }
            binding.imageView.setOnClickListener {
                if (!isItemNull()) {
                    getItem(position = bindingAdapterPosition)?.let { it1 ->
                        listener.onPhotoClick(photo = it1)
                    }
                }
            }

        }

        fun bind(photo: Photo) {
            binding.photoOwnerTxt.text = photo.user?.username
            binding.photoOwnerUsernameTxt.text =
                itemView.context.getString(R.string.username, photo.user?.username)

            if (photo.description != null) {
                binding.photoDescriptionTxt.visibility = View.VISIBLE
                binding.photoDescriptionTxt.text = photo.description
            } else
                binding.photoDescriptionTxt.visibility = View.GONE

            Glide.with(itemView)
                .load(photo.urls?.regular)
                .placeholder(photo.photoColor)
                .into(binding.imageView)

            Glide.with(itemView)
                .load(photo.user?.profileImage?.medium)
                .into(binding.photoOwnerProfilePic)
        }
    }

    /**
     * ViewHolder for our photo staggered grid.
     */
    inner class PhotoStaggeredGridViewHolder(private val binding: PhotoStaggeredGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onPhotoClick(photo = item)
                    }
                }
            }
        }

        fun bind(photo: Photo) {
            Glide.with(itemView)
                .load(photo.urls?.regular)
                .placeholder(photo.photoColor)
                .into(binding.imageView)
        }
    }

    /**
     * Photo click listener interface. We have two functions:
     *
     * 1. [onPhotoClick] is used when the user clicks on elements that navigate them to view the photo's details.
     * 2. [onPhotoOwnerClick] is used when the user clicks on elements that navigate them to photo's owner profile.
     */
    interface OnPhotoClickListener {
        /**
         * Handles the click when the photo is clicked.
         *
         * @param photo used to identify which photo has been clicked.
         */
        fun onPhotoClick(photo: Photo)

        /**
         * Handles the click on elements that navigate them to the photo's owner profile.
         *
         * @param photo used to identify which photo has been clicked.
         */
        fun onPhotoOwnerClick(photo: Photo)
    }

    /**
     * Compares the photos so as not to show them multiple times in the recyclerview.
     */
    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }

}