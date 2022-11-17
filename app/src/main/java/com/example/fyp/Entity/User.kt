package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class User(
    var id:String?= null,
    var name:String?= null,
    var gender:String? = null,
    var email:String? = null,

    var contactNo:Int?= null,
    var image:String?= null,

    var status: String?= null,
    var role:String?= null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(gender)
        parcel.writeString(email)
        parcel.writeValue(contactNo)
        parcel.writeString(image)
        parcel.writeString(status)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
