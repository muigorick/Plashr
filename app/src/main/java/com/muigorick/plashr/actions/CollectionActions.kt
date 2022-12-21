package com.muigorick.plashr.actions

import android.content.Context
import android.content.Intent
import com.muigorick.plashr.R
import com.muigorick.plashr.dataModels.collections.Collection

class CollectionActions(private val context: Context, private val collection: Collection) {

    /**
     * Collection sharing through intent.
     */
    fun shareCollection() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(
                R.string.share_collection_intent_message,
                collection.attributionUrl
            )
        )
        val shareIntent = Intent.createChooser(sendIntent, "Share collection")
        context.startActivity(shareIntent)
    }
}