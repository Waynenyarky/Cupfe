package com.tamayo_aaron_b.cupfe_expresso

data class OrderRequest(
    val reference_number: String,
    val username: String,
    val email: String,
    val total_amount: String,
    val status: String = "pending",
    val promo_code: String = "none",
    val order_type: String,
    val payment_method: String,
    val payment_status: String = "Unpaid",
    val est_time: String,
    val reason: String,
    val order_items: List<OrderItem>
)

data class OrderItem(
    val item_id: String,
    val item_name: String,
    val quantity: Int,
    val price: String,
    val size: String,
    val special_instructions: String,
    val username: String,
    val email: String
)