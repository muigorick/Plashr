package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.users.User
import com.muigorick.plashr.databinding.UserSearchItemBinding

class UsersRecyclerViewAdapter :
    PagingDataAdapter<User, UsersRecyclerViewAdapter.UserViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            UserSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UserViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = getItem(position = position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class UserViewHolder(private val binding: UserSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userFullNameTxt.text = user.name
            binding.userUsernameTxt.text = user.username
            Glide.with(itemView)
                .load(user.profileImage.mediumProfileImage)
                .error(R.drawable.ic_error_24)
                .into(binding.userProfilePic)
        }
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
        }
    }
}