package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class FoodStall(
    var name:String?= null,
    var opening:String? = null,
    var closing:String? = null,

    var location:String?= null,
    var image:String?= null,

    var status: String?= null,
    var cafeteriaID: String?=null,
    var sellerID: String?=null,

    var operatingDay: String? = null,
    var operatingHR: String? = null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.writeString(opening)
        parcel.writeString(closing)
        parcel.writeString(location)
        parcel.writeString(image)
        parcel.writeString(status)
        parcel.writeString(cafeteriaID)
        parcel.writeString(sellerID)
        parcel.writeString(operatingDay)
        parcel.writeString(operatingHR)
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
