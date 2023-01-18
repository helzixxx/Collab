package com.example.collab.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.io.Serializable

data class Genre constructor(
    var id: String? = "",
    var name: String? = "",
    var isChecked: Boolean = false
) : Serializable, Parcelable {

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeBoolean(isChecked)
    }

    companion object CREATOR : Parcelable.Creator<Genre> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Genre {
            return Genre(parcel)
        }

        override fun newArray(size: Int): Array<Genre?> {
            return arrayOfNulls(size)
        }
    }
}