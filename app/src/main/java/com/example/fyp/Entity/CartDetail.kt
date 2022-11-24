package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class CartDetail(
    var cartID:String?= null,
    var foodID:String?= null,
    var name:String?=null,
    var Remark:String?=null,
    var status:String?=null,

    var subtotal:Double?=null,
    var quantity:Int?= null,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cartID)
        parcel.writeString(foodID)
        parcel.writeString(name)
        parcel.writeString(Remark)
        parcel.writeString(status)
        parcel.writeValue(subtotal)
        parcel.writeValue(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartDetail> {
        override fun createFromParcel(parcel: Parcel): CartDetail {
            return CartDetail(parcel)
        }

        override fun newArray(size: Int): Array<CartDetail?> {
            return arrayOfNulls(size)
        }
    }
}
