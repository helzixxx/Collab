package com.example.collab.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import java.io.Serializable
import java.util.ArrayList

data class Person constructor(
    var id: String? = "",
    var name: String? = "",
    var surname: String? = "",
    var profileImage: String? = "",
    var dateOfBirth: String? = "",
    var profession: String? = "",
    var township: String? = "",
    var bio: String? = ""): Serializable, Parcelable {

//    @Exclude
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "id" to id,
//            "name" to name,
//            "surname" to surname,
//            "profileImage" to profileImage,
//            "age" to age,
//            "country" to country,
//            "bio" to bio
//        )
//    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(profileImage)
        parcel.writeString(dateOfBirth)
        parcel.writeString(profession)
        parcel.writeString(township)
        parcel.writeString(bio)
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