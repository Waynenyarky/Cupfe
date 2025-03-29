package com.tamayo_aaron_b.cupfe_expresso.tracking

import android.os.Parcel
import android.os.Parcelable

data class TrackConnection(
    val id: Int,
    val reference_number: String,
    val email: String,
    val username: String,
    val total_amount: String,
    val status: String,
    val promo_code: String,
    val order_type: String,
    val payment_method: String,
    val payment_status: String,
    val created_at: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(reference_number)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(total_amount)
        parcel.writeString(status)
        parcel.writeString(promo_code)
        parcel.writeString(order_type)
        parcel.writeString(payment_method)
        parcel.writeString(payment_status)
        parcel.writeString(created_at)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TrackConnection> {
        override fun createFromParcel(parcel: Parcel): TrackConnection = TrackConnection(parcel)
        override fun newArray(size: Int): Array<TrackConnection?> = arrayOfNulls(size)
    }
}