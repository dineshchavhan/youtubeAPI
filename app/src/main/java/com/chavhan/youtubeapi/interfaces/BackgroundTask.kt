package com.chavhan.youtubeapi.interfaces

import android.app.Activity

abstract class BackgroundTask(activity: Activity) {
    private val activity: Activity = activity
    private fun startBackground() {
        Thread {
            doInBackground()
            activity.runOnUiThread { onPostExecute() }
        }.start()
    }

    fun execute() {
        startBackground()
    }

    abstract fun doInBackground()
    abstract fun onPostExecute()

}