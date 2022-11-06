package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class FoodStall(
    var name:String?= null,


    var location:String?= null,
    var image:String?= null,

    var status: String?= null,
    var cafeteriaID: String?=null,
    var sellerID: String?=null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeString(image)
        parcel.writeString(status)
        parcel.writeString(cafeteriaID)
        parcel.writeString(sellerID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodStall> {
        override fun createFromParcel(parcel: Parcel): FoodStall {
            return FoodStall(parcel)
        }

        override fun newArray(size: Int): Array<FoodStall?> {
            return arrayOfNulls(size)
        }
    }
}
