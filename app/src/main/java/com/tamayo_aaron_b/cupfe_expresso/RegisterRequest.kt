package com.tamayo_aaron_b.cupfe_expresso

data class OTPRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: String = "customer"
)

data class VerifyOTPRequest(
    val username: String,
    val email: String,
    val password: String,
    val otp: String,
    val role: String = "customer" // Added role field
)