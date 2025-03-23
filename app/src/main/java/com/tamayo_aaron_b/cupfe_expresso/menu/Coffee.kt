package com.tamayo_aaron_b.cupfe_expresso.menu

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CoffeeResponse(
    val body: List<Coffee>
)

data class Coffee(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("subcategory") val subcategory: String,
    @SerializedName("is_available") val isAvailable: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("price_small") val priceSmall: String,
    @SerializedName("price_medium") val priceMedium: String,
    @SerializedName("price_large") val priceLarge: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(subcategory)
        parcel.writeInt(isAvailable)
        parcel.writeString(imageUrl)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(priceSmall)
        parcel.writeString(priceMedium)
        parcel.writeString(priceLarge)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Coffee> {
        override fun createFromParcel(parcel: Parcel): Coffee = Coffee(parcel)
        override fun newArray(size: Int): Array<Coffee?> = arrayOfNulls(size)
    }
}
