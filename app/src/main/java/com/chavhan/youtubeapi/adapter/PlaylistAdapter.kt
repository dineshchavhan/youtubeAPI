package com.chavhan.youtubeapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chavhan.youtubeapi.R
import com.chavhan.youtubeapi.interfaces.OnItemClickListener
import com.chavhan.youtubeapi.model.VdoModel
import com.squareup.picasso.Picasso


class PlaylistAdapter(private val context: Context, private val mlist:ArrayList<VdoModel>, private val listener: OnItemClickListener):
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    class ViewHolder(v: View):RecyclerView.ViewHolder(v) {

        val textViewTitle: TextView = v.findViewById(R.id.textViewTitle)
        val textViewDes: TextView = v.findViewById(R.id.textViewDes)
        val like: ImageButton = v.findViewById(R.id.like)
        val deete: ImageButton = v.findViewById(R.id.delete)
        val ImageThumb:ImageView = v.findViewById(R.id.ImageThumb)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_vdo,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set the views here
        val data:VdoModel = mlist[position]

        holder.textViewTitle.text = data.title
        holder.textViewDes.text = data.description
        holder.deete.setOnClickListener {
            listener.ondeleteClick(data, holder.deete)
        }
        holder.like.setOnClickListener {
            listener.onLikeClick(data, holder.like)
        }

        Picasso.with(context).load(data.thumbnail).into(holder.ImageThumb)

        holder.ImageThumb.setOnClickListener {
            listener.onItemClick(data)
        }
    }

    override fun getItemCount(): Int {
       return mlist.count()
    }

}