package com.example.collab.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Match constructor(
    var userId: String? = null,
    var name: String? = "",
    var lastMessage: String? = "",
    var profileImage: Uri?
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(lastMessage)
        parcel.writeParcelable(profileImage, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Match> {
        override fun createFromParcel(parcel: Parcel): Match {
            return Match(parcel)
        }

        override fun newArray(size: Int): Array<Match?> {
            return arrayOfNulls(size)
        }
    }

}