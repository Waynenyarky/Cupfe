package com.tamayo_aaron_b.cupfe_expresso

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class OTPRequests(val email: String)


data class ResetPasswordRequest(val email: String, val otp: String, val new_password: String)
data class ResetPasswordResponse(val success: Boolean, val message: String)

interface ApiService {
    @POST("api/users/create")
    fun sendOTP(@Body request: OTPRequest): Call<String>

    @POST("api/users/verify-otp")
    fun verifyOTP(@Body request: VerifyOTPRequest): Call<ApiResponse>

    @PUT("api/users/generate-password-change-otp")
    fun generatePasswordChangeOTP(@Body request: OTPRequests): Call<String>

    @PUT("api/users/change-password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<ResetPasswordResponse>

    @POST("api/orders/create-with-items")
    fun createOrder(@Body orderRequest: OrderRequest): Call<OrderResponse>

}