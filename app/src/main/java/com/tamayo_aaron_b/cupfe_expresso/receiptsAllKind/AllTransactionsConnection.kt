package com.tamayo_aaron_b.cupfe_expresso.receiptsAllKind

import android.os.Parcel
import android.os.Parcelable

data class AllTransactionsConnection(
    val id: Int,
    val email: String,
    val reference_number: String,
    val receipt_text: String,
    val created_at: String,
    val receipt_for: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(email)
        parcel.writeString(reference_number)
        parcel.writeString(receipt_text)
        parcel.writeString(created_at)
        parcel.writeString(receipt_for)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AllTransactionsConnection> {
        override fun createFromParcel(parcel: Parcel): AllTransactionsConnection = AllTransactionsConnection(parcel)
        override fun newArray(size: Int): Array<AllTransactionsConnection?> = arrayOfNulls(size)
    }
}
