package com.chavhan.youtubeapi.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chavhan.youtubeapi.DetailsActivity
import com.chavhan.youtubeapi.R
import com.chavhan.youtubeapi.adapter.PlaylistAdapter
import com.chavhan.youtubeapi.interfaces.OnItemClickListener
import com.chavhan.youtubeapi.model.VdoModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavouriteFragment : Fragment() {
    companion object{
        var favList = ArrayList<VdoModel>()
        var favRecycler: RecyclerView?=null
    }
    private var msgbox: TextView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        favRecycler = view.findViewById(R.id.fav_recycler)
        msgbox = view.findViewById(R.id.fav_msg)



        initview(favList)
        return view
    }

    private fun initview(list:ArrayList<VdoModel>) {
        favRecycler!!.layoutManager = LinearLayoutManager(activity)
        val mVdoAdapter = PlaylistAdapter(requireActivity(),list, object : OnItemClickListener {
            override fun onItemClick(item: VdoModel?) {
                val youtubeDataModel: VdoModel = item!!
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra(VdoModel::class.java.toString(), youtubeDataModel)
                startActivity(intent)
            }

            override fun onLikeClick(item: VdoModel?, like: ImageButton) {

            }

            override fun ondeleteClick(item: VdoModel?, like: ImageButton) {
                favList.remove(item)
                val pref = requireActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE)
                val json = Gson().toJson(favList)
                pref.edit().putString("fav", json).apply()
                favRecycler!!.adapter!!.notifyDataSetChanged()
            }
        })
        favRecycler!!.adapter = mVdoAdapter

    }
    override fun onResume() {
        super.onResume()

        val pref = requireActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE)
        val json = pref.getString("fav", "")
        favList = if(json.isNullOrEmpty()) {
            ArrayList()
        }else{
            Gson().fromJson(json, object:TypeToken<ArrayList<VdoModel>>(){}.type)
        }
        initview(favList)
        if (favList.isEmpty()) {
            msgbox!!.text = String.format("Empty Favourite List")
        }else{
            msgbox!!.text = String.format("")
        }
    }

}