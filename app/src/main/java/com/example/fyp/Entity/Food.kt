package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Food(
    var name:String?= null,
    var description:String?= null,


    var price:Double?= null,
    var type:String?= null,

    var image:String?= null,
    var calorie:Double?= null,

    var status: String?= null,
    var foodstallID: String?= null,

    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeValue(price)
        parcel.writeString(type)
        parcel.writeString(image)
        parcel.writeValue(calorie)
        parcel.writeString(status)
        parcel.writeString(foodstallID)
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