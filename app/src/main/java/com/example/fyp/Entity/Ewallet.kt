package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class Ewallet(
    var walletID:String?= null,
    var userID:String?= null,
    var status:String?=null,
    var pinNo:String?=null,

    var balance:String?=null,


):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(walletID)
        parcel.writeString(userID)
        parcel.writeString(status)
        parcel.writeString(pinNo)
        parcel.writeString(balance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ewallet> {
        override fun createFromParcel(parcel: Parcel): Ewallet {
            return Ewallet(parcel)
        }

        override fun newArray(size: Int): Array<Ewallet?> {
            return arrayOfNulls(size)
        }
    }
}
