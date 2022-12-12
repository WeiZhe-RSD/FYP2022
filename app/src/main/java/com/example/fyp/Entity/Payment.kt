package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Payment(
    var paymentID:String?= null,
    var amount:String? = null,
    var type:String? = null,

    var date:String?= null,
    var status:String?= null,

    var orderID: String? = null,
):Parcelable {
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
        parcel.writeString(paymentID)
        parcel.writeString(amount)
        parcel.writeString(type)
        parcel.writeString(date)
        parcel.writeString(status)
        parcel.writeString(orderID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Payment> {
        override fun createFromParcel(parcel: Parcel): Payment {
            return Payment(parcel)
        }

        override fun newArray(size: Int): Array<Payment?> {
            return arrayOfNulls(size)
        }
    }
}
