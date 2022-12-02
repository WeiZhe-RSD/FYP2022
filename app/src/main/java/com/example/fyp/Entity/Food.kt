package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Food(
    var name:String?= null,
    var description:String?= null,
    var image:String?= null,

    var type:String?= null,
    var calories:Int?= null,
    var status: String?= null,
    var foodstallID: String?= null,
    var foodID:String?=null,
    var price:Double?= null,

    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),

        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)

        parcel.writeString(type)

        parcel.writeValue(calories)
        parcel.writeString(status)
        parcel.writeString(foodstallID)
        parcel.writeString(foodID)
        parcel.writeValue(price)
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