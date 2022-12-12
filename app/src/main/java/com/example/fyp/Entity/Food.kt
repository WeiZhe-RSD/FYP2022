package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Food(
    var foodID:String?=null,
    var name:String?= null,
    var description:String?= null,
    var image:String?= null,

    var type:String?= null,
    var calories:String?= null,
    var status: String?= null,
    var foodstallID: String?= null,

    var price:String?= null,

    ): Parcelable {
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
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodID)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)

        parcel.writeString(type)

        parcel.writeString(calories)
        parcel.writeString(status)
        parcel.writeString(foodstallID)

        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}