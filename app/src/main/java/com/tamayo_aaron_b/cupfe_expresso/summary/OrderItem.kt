package com.tamayo_aaron_b.cupfe_expresso.summary

import android.os.Parcel
import android.os.Parcelable

data class OrderItem(
    val id:Int,
    val coffeeName: String,
    val selectedSize: String,
    var quantity: Int,
    val userComment: String,
    val price: Double,
    val imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble() ,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(coffeeName)
        parcel.writeString(selectedSize)
        parcel.writeInt(quantity)
        parcel.writeString(userComment)
        parcel.writeDouble(price)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }
}