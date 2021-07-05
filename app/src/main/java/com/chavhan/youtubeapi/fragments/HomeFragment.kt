package com.chavhan.youtubeapi.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chavhan.youtubeapi.DetailsActivity
import com.chavhan.youtubeapi.R
import com.chavhan.youtubeapi.adapter.PlaylistAdapter
import com.chavhan.youtubeapi.interfaces.OnItemClickListener
import com.chavhan.youtubeapi.model.VdoModel
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {
//    private val GOOGLE_YOUTUBE_API_KEY =
//    "AIzaSyDzkL0ph3zPrEbfEt-qoWuN-wju5eZxwhw" //here you should use your api key for testing purpose you can use this api also
//    private val CHANNEL_ID =
//        "UCsgUHj6J1MKAGXHD-jL3K7g" //here you should use your channel id for testing purpose you can use this api also

private var GOOGLE_YOUTUBE_API_KEY: String? =
    "AIzaSyB_HYsrnM2irNfSKQvrVNfL-ZVM-ROPxWw" //here you should use your api key for testing purpose you can use this api also

    private val CHANNEL_ID =
        "UCL99o_jabQobcK5FNSBGTcg" //here you should use your channel id for testing purpose you can use this api also


    private val CHANNLE_GET_URL ="https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=$CHANNEL_ID&maxResults=20&key=$GOOGLE_YOUTUBE_API_KEY"
    private var mList = ArrayList<VdoModel>()
    private var favList = ArrayList<VdoModel>()
    private var homeRecycler:RecyclerView?=null

    var msgbox:TextView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
         homeRecycler = view.findViewById(R.id.home_recycler)
        msgbox = view.findViewById(R.id.homemsg)
        initview(mList)
        displayvideos()
        return view
    }


    private fun initview(list:ArrayList<VdoModel>) {
        homeRecycler!!.layoutManager = LinearLayoutManager(activity)
        val mVdoAdapter = PlaylistAdapter(requireActivity(),list, object : OnItemClickListener{
            override fun onItemClick(item: VdoModel?) {
                val youtubeDataModel: VdoModel = item!!
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra(VdoModel::class.java.toString(), youtubeDataModel)
                startActivity(intent)
                PlaylistFragment.watchList.add(item)
                PlaylistFragment.watchRecycler!!.adapter!!.notifyDataSetChanged()
            }

            @SuppressLint("CommitPrefEdits")
            override fun onLikeClick(item: VdoModel?, like: ImageButton) {
                if (item != null) {
                    favList = FavouriteFragment.favList
                    favList.add(item)
                    like.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null))
                    val pref = requireActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE)
                    val json = Gson().toJson(favList)
                    pref.edit().putString("fav", json).apply()
                }
            }

            override fun ondeleteClick(item: VdoModel?, like: ImageButton) {

            }
        })
        homeRecycler!!.adapter = mVdoAdapter
    }
//    override fun onResume() {
//        super.onResume()
//        if (mList.isEmpty()) {
//            msgbox!!.text = String.format("EmptyWatch List")
//        }else{
//            msgbox!!.text = String.format("")
//        }
//    }

    private fun displayvideos() {
        val requestQueue =
            Volley.newRequestQueue(requireContext())
        val stringRequest = StringRequest(Request.Method.GET, CHANNLE_GET_URL,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("items")
                    for (i in 0..jsonArray.length()) {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val jsonObject2 = jsonObject1.getJSONObject("id")
                        val jsonObject4 = jsonObject1.getJSONObject("snippet")
                        val jsonObject5 = jsonObject4.getJSONObject("thumbnails")
                        val jsonObject6 = jsonObject5.getJSONObject("medium")
                        val Video_id = jsonObject2.getString("videoId")

                        val youtubeObject = VdoModel()
                        youtubeObject.title = jsonObject4.getString("title")
                        youtubeObject.description = jsonObject4.getString("description")
                        youtubeObject.publishedAt = jsonObject4.getString("publishedAt")
                        youtubeObject.thumbnail = jsonObject4.getJSONObject("thumbnails").getJSONObject("high")
                            .getString("url")
                        youtubeObject.video_id = Video_id
                        mList.add(youtubeObject)
                        homeRecycler!!.adapter!!.notifyDataSetChanged()
                        if (mList.isEmpty()) {
                            msgbox!!.text = String.format("Empty List")
                        }else{
                            msgbox!!.text = String.format("")
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(
                    requireContext(),
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
                if (mList.isEmpty()) {
                    msgbox!!.text = String.format("Empty List")
                }else{
                    msgbox!!.text = String.format("")
                }
            })
        requestQueue.add(stringRequest)

    }




//    @SuppressLint("StaticFieldLeak")
//    private inner class RequestYoutubeAPI(activity: Activity) :
//       BackgroundTask(activity)
////        AsyncTask<Void?, String?, String?>()
//    {
//
////        override fun doInBackground(vararg params: Void?): String? {
////            val httpClient: HttpClient = DefaultHttpClient()
////            val httpGet = HttpGet(CHANNLE_GET_URL)
////            Log.e("URL", CHANNLE_GET_URL)
////            try {
////                val response = httpClient.execute(httpGet)
////                val httpEntity = response.entity
////                return EntityUtils.toString(httpEntity)
////            } catch (e: IOException) {
////                e.printStackTrace()
////            }
////            return null
////        }
////
////   override fun onPostExecute(response: String?) {
////            super.onPostExecute(response)
////            if (response != null) {
////                try {
////                    val jsonObject = JSONObject(response)
////                    Log.e("response", jsonObject.toString())
////                    mList = parseVideoListFromResponse(jsonObject)
////                    initview(mList)
////                } catch (e: JSONException) {
////                    e.printStackTrace()
////                }
////            }
////        }
//
//
//        override fun doInBackground() {
//            val httpClient: HttpClient = DefaultHttpClient()
//            val httpGet = HttpGet(CHANNLE_GET_URL)
//            Log.e("URL", CHANNLE_GET_URL)
//            try {
//                val response = httpClient.execute(httpGet)
//                val httpEntity = response.entity
//                datastring =  EntityUtils.toString(httpEntity)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//
//        override fun onPostExecute() {
//            try {
//                val jsonObject = JSONObject(datastring)
//                Log.e("response", jsonObject.toString())
//                for (i in parseVideoListFromResponse(jsonObject))
//                    mList.add(i)
//               // initview(mList)
//                homeRecycler!!.adapter!!.notifyDataSetChanged()
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }
//    }

//    fun parseVideoListFromResponse(jsonObject: JSONObject): ArrayList<VdoModel> {
//        val list = ArrayList<VdoModel>()
//        if (jsonObject.has("items")) {
//            try {
//                val jsonArray = jsonObject.getJSONArray("items")
//                for (i in 0 until jsonArray.length()) {
//                    val json = jsonArray.getJSONObject(i)
//                    if (json.has("id")) {
//                        val jsonID = json.getJSONObject("id")
//                        var video_id: String? = ""
//                        if (jsonID.has("videoId")) {
//                            video_id = jsonID.getString("videoId")
//                        }
//                        if (jsonID.has("kind")) {
//                            if (jsonID.getString("kind") == "youtube#video") {
//                                val youtubeObject = VdoModel()
//                                val jsonSnippet = json.getJSONObject("snippet")
//                                val title = jsonSnippet.getString("title")
//                                val description = jsonSnippet.getString("description")
//                                val publishedAt = jsonSnippet.getString("publishedAt")
//                                val thumbnail =
//                                    jsonSnippet.getJSONObject("thumbnails").getJSONObject("high")
//                                        .getString("url")
//                                youtubeObject.title = title
//                                youtubeObject.description = description
//                                youtubeObject.publishedAt = publishedAt
//                                youtubeObject.thumbnail = thumbnail
//                                youtubeObject.video_id = video_id
//                                list.add(youtubeObject)
//                            }
//                        }
//                    }
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }
//        return list
//    }


}