package com.chavhan.youtubeapi.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chavhan.youtubeapi.DetailsActivity
import com.chavhan.youtubeapi.R
import com.chavhan.youtubeapi.adapter.PlaylistAdapter
import com.chavhan.youtubeapi.interfaces.OnItemClickListener
import com.chavhan.youtubeapi.model.VdoModel
import com.google.gson.Gson
import java.util.prefs.Preferences


class PlaylistFragment : Fragment() {
    companion object{
        var watchList = ArrayList<VdoModel>()
        var watchRecycler: RecyclerView?=null
    }
    private var favList = ArrayList<VdoModel>()
    private var msgbox:TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        watchRecycler = view.findViewById(R.id.ply_recycler)
        msgbox = view.findViewById(R.id.ply_msg)
        initview(watchList)

        return view
    }

    override fun onResume() {
        super.onResume()
        if (watchList.isEmpty()) {
            msgbox!!.text = String.format("Empty Watch List")
        }else{
            msgbox!!.text = String.format("")
        }
    }

    private fun initview(list:ArrayList<VdoModel>) {
        watchRecycler!!.layoutManager = LinearLayoutManager(activity)
        val mVdoAdapter = PlaylistAdapter(requireActivity(),list, object : OnItemClickListener {
            override fun onItemClick(item: VdoModel?) {
                val youtubeDataModel: VdoModel = item!!
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra(VdoModel::class.java.toString(), youtubeDataModel)
                startActivity(intent)
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
        watchRecycler!!.adapter = mVdoAdapter

    }


}


