package com.muigorick.plashr.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

class Utils(private val context: Context) {

    /**
     *  Handles the deeplink URL.
     *
     *  @param intent The intent containing the URL we intend to fetch the data from.
     *  @return Returns the URI that is to be used to fetch the data out of. I
     */
     fun handleDeeplinkIntent(intent: Intent): Uri?{
        val action: String? = intent.action
        val data = Uri.parse((intent.data).toString())
        if (Intent.ACTION_VIEW == action && data != null) {
            return data
        }
        return null
    }

    /**
     * Opens a web page.
     *
     * @param url website link to be opened
     * @param packageManager
     */
     fun openWebPage(url: String,packageManager:PackageManager) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(intent)
        }
    }

}