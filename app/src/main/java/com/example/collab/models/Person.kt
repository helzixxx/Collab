package com.example.collab.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import java.io.Serializable
import java.util.ArrayList

data class Person constructor(
    var name: String? = "",
    var surname: String? = "",
    var dateOfBirth: String? = "",
    var profession: String? = "",
    var township: String? = "",
    var profileImage: String? = "",
    var bio: String? = ""
    //var genres: ArrayList<String>? = null
    ): Serializable, Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        //parcel.createStringArrayList()!!
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
        //parcel.writeArray(arrayOf(genres))
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