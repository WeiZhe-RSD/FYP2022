package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class CartDetail(
    var cartID:String?= null,
    var foodID:String?= null,
    var name:String?=null,
    var detailID:String?=null,
    var Remark:String?=null,
    var status:String?=null,
    var orderID:String?=null,

    var subtotal:String?=null,
    var quantity:String?= null,


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
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cartID)
        parcel.writeString(foodID)
        parcel.writeString(name)
        parcel.writeString(detailID)
        parcel.writeString(Remark)
        parcel.writeString(status)
        parcel.writeString(orderID)
        parcel.writeString(subtotal)
        parcel.writeString(quantity)
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
