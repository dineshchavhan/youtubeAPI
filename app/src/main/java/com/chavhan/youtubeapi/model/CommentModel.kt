package com.chavhan.youtubeapi.model

import android.os.Parcel
import android.os.Parcelable


 class CommentModel() :Parcelable{
     var title = ""
     var comment = ""
     var publishedAt = ""
     var thumbnail = ""
     var video_id = ""

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(comment)
        parcel.writeString(publishedAt)
        parcel.writeString(thumbnail)
        parcel.writeString(video_id)
    }
    protected fun CommentModel(`in`: Parcel) {
        readFromParcel(`in`)
    }

    fun readFromParcel(`in`: Parcel) {
        title = `in`.readString()!!
        comment = `in`.readString()!!
        publishedAt = `in`.readString()!!
        thumbnail = `in`.readString()!!
        video_id = `in`.readString()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommentModel> {
        override fun createFromParcel(parcel: Parcel): CommentModel {
            return CommentModel(parcel)
        }

        override fun newArray(size: Int): Array<CommentModel?> {
            return arrayOfNulls(size)
        }
    }

}





