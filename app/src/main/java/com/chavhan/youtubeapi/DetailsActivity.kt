package com.chavhan.youtubeapi

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YouTubeUriExtractor
import at.huber.youtubeExtractor.YtFile
import com.chavhan.youtubeapi.adapter.CommentAdapter
import com.chavhan.youtubeapi.model.CommentModel
import com.chavhan.youtubeapi.model.VdoModel
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayerView

import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*

class DetailsActivity:YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val READ_STORAGE_PERMISSION_REQUEST_CODE = 1
    private val GOOGLE_YOUTUBE_API = "AIzaSyBH8szUCt1ctKQabVeQuvWgowaKxHVjn8E"
    private var youtubeDataModel: VdoModel? = null
    var textViewName: TextView? = null
    var textViewDes: TextView? = null
    var textViewDate: TextView? = null

       // ImageView imageViewIcon;
    val VIDEO_ID = "c2UNv38V6y4"
    private var mYoutubePlayerView: YouTubePlayerView? = null
    private var mYoutubePlayer: YouTubePlayer? = null
    private var mListData: ArrayList<CommentModel>? = null
    private var mAdapter: CommentAdapter? = null
    private var mList_videos: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        youtubeDataModel = intent.getParcelableExtra(VdoModel::class.java.toString())
        Log.e("", youtubeDataModel!!.description!!)
        mYoutubePlayerView = findViewById<View>(R.id.youtube_player) as YouTubePlayerView
        mYoutubePlayerView!!.initialize(GOOGLE_YOUTUBE_API, this)
        textViewName = findViewById<View>(R.id.textViewName) as TextView
        textViewDes = findViewById<View>(R.id.textViewDes) as TextView
        // imageViewIcon = (ImageView) findViewById(R.id.imageView);
        textViewDate = findViewById<View>(R.id.textViewDate) as TextView
        textViewName!!.setText(youtubeDataModel!!.title)
        textViewDes!!.setText(youtubeDataModel!!.description)
        textViewDate!!.setText(youtubeDataModel!!.publishedAt)
        mList_videos = findViewById<View>(R.id.mList_videos) as RecyclerView
        RequestYoutubeCommentAPI().execute()

        if (!checkPermissionForReadExtertalStorage()) {
            try {
                requestPermissionForReadExtertalStorage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun back_btn_pressed(view: View?) {
        finish()
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer,
        wasRestored: Boolean
    ) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer.setPlaybackEventListener(playbackEventListener)
        if (!wasRestored) {
            youTubePlayer.cueVideo(youtubeDataModel!!.video_id)
        }
        mYoutubePlayer = youTubePlayer
    }

    private val playbackEventListener: PlaybackEventListener = object : PlaybackEventListener {
        override fun onPlaying() {}
        override fun onPaused() {}
        override fun onStopped() {}
        override fun onBuffering(b: Boolean) {}
        override fun onSeekTo(i: Int) {}
    }

    private val playerStateChangeListener: PlayerStateChangeListener =
        object : PlayerStateChangeListener {
            override fun onLoading() {}
            override fun onLoaded(s: String) {}
            override fun onAdStarted() {}
            override fun onVideoStarted() {}
            override fun onVideoEnded() {}
            override fun onError(errorReason: YouTubePlayer.ErrorReason) {}
        }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
    }

    fun share_btn_pressed(view: View?) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val link = "https://www.youtube.com/watch?v=" + youtubeDataModel!!.video_id
        // this is the text that will be shared
        sendIntent.putExtra(Intent.EXTRA_TEXT, link)
        sendIntent.putExtra(
            Intent.EXTRA_SUBJECT, youtubeDataModel!!.title
                .toString() + "Share"
        )
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "share"))
    }

    @SuppressLint("StaticFieldLeak")
    fun downloadVideo(view: View?) {
        //get the download URL
        val youtubeLink = "https://www.youtube.com/watch?v=" + youtubeDataModel!!.video_id
        val ytEx: YouTubeExtractor = object : YouTubeUriExtractor(this) {
            override fun onUrisAvailable(
                videoID: String?,
                videoTitle: String?,
                ytFiles: SparseArray<YtFile>?
            ) {
                if (ytFiles != null) {
                    val itag = 22
                    //This is the download URL
                    val downloadURL: String = ytFiles[itag].url
                    Log.e("download URL :", downloadURL)

                    //now download it like a file
                    RequestDownloadVideoStream().execute(downloadURL, videoTitle)
                }
            }
        }
        ytEx.execute(youtubeLink)
    }

    private val pDialog: ProgressDialog? = null


    @SuppressLint("StaticFieldLeak")
    private inner class RequestDownloadVideoStream :
        AsyncTask<String?, String?, String?>() {
        val pDialog = ProgressDialog(baseContext)
        override fun onPreExecute() {
            super.onPreExecute()

            pDialog.setMessage("Downloading file. Please wait...")
            pDialog.setIndeterminate(false)
            pDialog.setMax(100)
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            pDialog.setCancelable(false)
            pDialog.show()
        }

         override fun doInBackground(vararg params: String?): String? {
            var `is`: InputStream? = null
            var u: URL? = null
            var len1 = 0
            var temp_progress = 0
            var progress = 0
            try {
                u = URL(params[0])
                `is` = u.openStream()
                val huc = u.openConnection() as URLConnection
                huc.connect()
                val size = huc.contentLength
                if (huc != null) {
                    val file_name = params[1] + ".mp4"
                    val storagePath =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString() + "/YoutubeVideos"
                    val f = File(storagePath)
                    if (!f.exists()) {
                        f.mkdir()
                    }
                    val fos = FileOutputStream("$f/$file_name")
                    val buffer = ByteArray(1024)
                    var total = 0
                    if (`is` != null) {
                        while (`is`.read(buffer).also { len1 = it } != -1) {
                            total += len1
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            progress = (total * 100 / size)
                            if (progress >= 0) {
                                temp_progress = progress
                                publishProgress("" + progress)
                            } else publishProgress("" + temp_progress + 1)
                            fos.write(buffer, 0, len1)
                        }
                    }
                    if (fos != null) {
                        publishProgress("" + 100)
                        fos.close()
                    }
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (`is` != null) {
                    try {
                        `is`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return null
        }

         override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            pDialog.setProgress(values[0]!!.toInt())
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (pDialog.isShowing()) pDialog.dismiss()
        }
    }


    private inner class RequestYoutubeCommentAPI :
        AsyncTask<Void?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

         override fun doInBackground(vararg params: Void?): String? {
//            val VIDEO_COMMENT_URL =
//                "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&maxResults=100&videoId=" + youtubeDataModel!!.video_id
//                    .toString() + "&key=" + GOOGLE_YOUTUBE_API
//            val httpClient: HttpClient = DefaultHttpClient()
//            val httpGet = HttpGet(VIDEO_COMMENT_URL)
//            Log.e("url: ", VIDEO_COMMENT_URL)
//            try {
//                val response = httpClient.execute(httpGet)
//                val httpEntity = response.entity
//                return EntityUtils.toString(httpEntity)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
            return null
        }

        override fun onPostExecute(response: String?) {
            super.onPostExecute(response)
            if (response != null) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.e("response", jsonObject.toString())
                    mListData = parseJson(jsonObject) as ArrayList<CommentModel>
                    initVideoList(mListData!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun initVideoList(mListData: ArrayList<CommentModel>) {
        mList_videos!!.layoutManager = LinearLayoutManager(this)
        mAdapter = CommentAdapter(this, mListData)
        mList_videos!!.adapter = mAdapter
    }

    fun parseJson(jsonObject: JSONObject): ArrayList<CommentModel>? {
        val mList: ArrayList<CommentModel> = ArrayList<CommentModel>()
        if (jsonObject.has("items")) {
            try {
                val jsonArray = jsonObject.getJSONArray("items")
                for (i in 0 until jsonArray.length()) {
                    val json = jsonArray.getJSONObject(i)
                    val youtubeObject = CommentModel()
                    val jsonTopLevelComment =
                        json.getJSONObject("snippet").getJSONObject("topLevelComment")
                    val jsonSnippet = jsonTopLevelComment.getJSONObject("snippet")
                    val title = jsonSnippet.getString("authorDisplayName")
                    val thumbnail = jsonSnippet.getString("authorProfileImageUrl")
                    val comment = jsonSnippet.getString("textDisplay")
                    youtubeObject.title = (title)
                    youtubeObject.comment = (comment)
                    youtubeObject.thumbnail = (thumbnail)
                    mList.add(youtubeObject)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return mList
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                (this as Activity),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val result2 = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
        }
        return false
    }
}