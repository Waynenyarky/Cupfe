package com.tamayo_aaron_b.cupfe_expresso.menu

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
)
