package com.tamayo_aaron_b.cupfe_expresso.reservation

import com.google.gson.annotations.SerializedName


data class ReservationConnect(
    @SerializedName("reference_number") val transactionId: String,
    @SerializedName("username") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val cellphone: String,
    @SerializedName("Bundle") val bundle: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("reservation_date") val date: String,
    @SerializedName("reservation_time") val time: String
)