import android.os.Parcel
import android.os.Parcelable

data class FavoriteItem(
    val coffeeName: String,
    val selectedSize: String,
    var quantity: Int,
    val userComment: String,
    val price: String,
    val imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(coffeeName)
        parcel.writeString(selectedSize)
        parcel.writeInt(quantity)
        parcel.writeString(userComment)
        parcel.writeString(price)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FavoriteItem> {
        override fun createFromParcel(parcel: Parcel): FavoriteItem {
            return FavoriteItem(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteItem?> {
            return arrayOfNulls(size)
        }
    }
}

