package com.chavhan.youtubeapi.model

import android.os.Parcel
import android.os.Parcelable


class VdoModel() :Parcelable{
    var title: String? = ""
    var description: String? = ""
    var publishedAt: String? = ""
    var thumbnail: String? = ""
    var video_id: String? = ""

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        publishedAt = parcel.readString()
        thumbnail = parcel.readString()
        video_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(publishedAt)
        parcel.writeString(thumbnail)
        parcel.writeString(video_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VdoModel> {
        override fun createFromParcel(parcel: Parcel): VdoModel {
            return VdoModel(parcel)
        }

        override fun newArray(size: Int): Array<VdoModel?> {
            return arrayOfNulls(size)
        }
    }
}





