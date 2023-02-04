package com.example.collab.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

data class Person(
    var name: String? = "",
    var surname: String? = "",
    var dateOfBirth: String? = "",
    var profession: String? = "",
    var township: String? = "",
    var profileImage: String? = "",
    var bio: String? = "",
    var genres: ArrayList<Genre?>? = null,
    var instruments: ArrayList<Instrument?>? = null
    ): Serializable, Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Genre.CREATOR)!!,
        parcel.createTypedArrayList(Instrument.CREATOR)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(dateOfBirth)
        parcel.writeString(profession)
        parcel.writeString(township)
        parcel.writeString(profileImage)
        parcel.writeString(bio)
        parcel.writeTypedList(genres)
        parcel.writeTypedList(instruments)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}