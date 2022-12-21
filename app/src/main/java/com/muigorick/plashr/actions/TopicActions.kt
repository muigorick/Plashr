package com.muigorick.plashr.actions

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.core.text.HtmlCompat
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.photos.Photo
import com.muigorick.plashr.dataModels.topics.Topic
import java.io.File
import java.util.*


class TopicActions(private val context: Context, private val topic: Topic) {

    /**
     * Topic sharing through intent.
     */
    fun shareTopic() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(
                R.string.share_topic_intent_message, topic.links.html
            )
        )
        val shareIntent = Intent.createChooser(sendIntent, "Share topic")
        context.startActivity(shareIntent)
    }
}