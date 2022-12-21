package com.muigorick.plashr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muigorick.plashr.dataModels.topics.Topic
import com.muigorick.plashr.databinding.TopicCardItemBinding

/**
 * Topics recyclerview adapter.
 *
 * It is reused whenever we fetch paginated topics that are meant to be displayed in a recyclerview.
 *
 * @param listener The listener used to handle the click listeners.
 *
 */

class TopicsRecyclerViewAdapter(private val listener: OnTopicClickListener) :
    PagingDataAdapter<Topic, TopicsRecyclerViewAdapter.TopicViewHolder>(
        TOPIC_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding =
            TopicCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TopicViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val currentItem = getItem(position = position)
        if (currentItem != null)
            holder.bind(currentItem)
    }

    /**
     * ViewHolder for our topics.
     */
    inner class TopicViewHolder(private val binding: TopicCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
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

            binding.root.setOnClickListener {
                if (!isItemNull()) {
                    getItem(bindingAdapterPosition)?.let { topic -> listener.onTopicClick(topic = topic) }
                }
            }

            binding.root.setOnLongClickListener {
                if (!isItemNull()) {
                    getItem(bindingAdapterPosition)?.let { topic ->
                        listener.onTopicLongClick(
                            topic = topic
                        )
                    }
                }
                true
            }
        }

        fun bind(topic: Topic) {
            Glide.with(itemView)
                .load(topic.coverPhoto.urls.regular)
                .placeholder(topic.coverPhoto.coverPhotoColor)
                .into(binding.topicCoverImage)
            binding.topicTitleTxt.text = topic.title
        }
    }

    /**
     * Topic click listener interface. We have two functions:
     *
     * 1. [onTopicClick] is used when the user single clicks on the topic layout.
     * 2. [onTopicLongClick] is used when the user long clicks on the topic layout.
     */
    interface OnTopicClickListener {
        /**
         * Handles the click when the topic is clicked.
         *
         * @param topic used to identify which topic has been clicked.
         */
        fun onTopicClick(topic: Topic)

        /**
         * Handles the click when the topic is long clicked.
         *
         * @param topic used to identify which topic has been long clicked.
         */
        fun onTopicLongClick(topic: Topic)
    }
    /**
     * Compares the topics so as not to show them multiple times in the recyclerview.
     */
    companion object {
        private val TOPIC_COMPARATOR = object : DiffUtil.ItemCallback<Topic>() {
            override fun areItemsTheSame(oldItem: Topic, newItem: Topic) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Topic, newItem: Topic) = oldItem == newItem
        }
    }
}