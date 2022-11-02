package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Cafeteria(
    var name:String?= null,


    var latitude:Double?= null,
    var longtitude:Double?= null,
    var image:String?= null,

    var status: String?= null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),


    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(latitude!!)
        parcel.writeDouble(longtitude!!)
        parcel.writeString(image)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cafeteria> {
        override fun createFromParcel(parcel: Parcel): Cafeteria {
            return Cafeteria(parcel)
        }

        override fun newArray(size: Int): Array<Cafeteria?> {
            return arrayOfNulls(size)
        }
    }
}
