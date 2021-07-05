package com.chavhan.youtubeapi.interfaces

import android.view.View
import android.widget.ImageButton
import com.chavhan.youtubeapi.model.VdoModel

interface OnItemClickListener {
    fun onItemClick(item: VdoModel?)
    fun onLikeClick(item: VdoModel?, like: ImageButton)
    fun ondeleteClick(item: VdoModel?, like: ImageButton)

}