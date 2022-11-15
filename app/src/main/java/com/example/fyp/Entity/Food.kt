package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Food(
    var name:String?= null,
    var description:String?= null,
    var image:String?= null,
    //var price:Double?= null,
    var type:String?= null,


    var calorie:Int?= null,

    var status: String?= null,
    var foodstallID: String?= null,
    var foodID:String?=null

    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        //parcel.readDouble(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)
        //parcel.writeValue(price)
        parcel.writeString(type)

        parcel.writeValue(calorie)
        parcel.writeString(status)
        parcel.writeString(foodstallID)
        parcel.writeString(foodID)
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