package com.example.fyp.Entity

import android.os.Parcel
import android.os.Parcelable

data class EatTogehter(
    var eatTogehterID:String?= null,
    var dateCreated:String?= null,
    var status:String?=null,
    var customerID:String?=null,
    var invitedID:String?=null,
    var dateMeet:String?=null,
    var venue:String?=null,


    ):Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.writeString(eatTogehterID)
        parcel.writeString(dateCreated)
        parcel.writeString(status)
        parcel.writeString(customerID)
        parcel.writeString(invitedID)
        parcel.writeString(dateMeet)
        parcel.writeString(venue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EatTogehter> {
        override fun createFromParcel(parcel: Parcel): EatTogehter {
            return EatTogehter(parcel)
        }

        override fun newArray(size: Int): Array<EatTogehter?> {
            return arrayOfNulls(size)
        }
    }
}
