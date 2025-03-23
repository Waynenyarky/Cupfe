package com.tamayo_aaron_b.cupfe_expresso.menu

import com.google.gson.annotations.SerializedName

data class CoffeeResponse(
    val body: List<Coffee>
)

data class Coffee(
    @SerializedName("product_id") val id: Int,
    @SerializedName("product_name") val name: String,
    @SerializedName("product_image") val imageUrl: String,
    @SerializedName("product_price") val price: Int,
    @SerializedName("product_price") val subcategory: String
)
