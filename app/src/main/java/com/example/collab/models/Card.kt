package com.example.collab.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

data class Card constructor(
    var userId: String? = null,
    var name: String? = "",
    var profession: String? = "",
    var profileImage: String? = "",
    var genres: ArrayList<Genre?>? = null,
    var instruments: ArrayList<Instrument?>? = null

) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Genre.CREATOR)!!,
        parcel.createTypedArrayList(Instrument.CREATOR)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(profession)
        parcel.writeString(profileImage)
        parcel.writeTypedList(genres)
        parcel.writeTypedList(instruments)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}