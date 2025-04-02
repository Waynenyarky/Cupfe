package com.tamayo_aaron_b.cupfe_expresso.cartFolder

import java.io.Serializable

data class CartItem(
    val id: Int,
    val coffeeName: String,
    val selectedSize: String,
    var quantity: Int,
    val userComment: String,
    val price: Double,
    val imageUrl: String?,
    var isSelected: Boolean = false // Added this property
): Serializable

