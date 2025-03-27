package com.tamayo_aaron_b.cupfe_expresso

import com.google.gson.annotations.SerializedName
import com.tamayo_aaron_b.cupfe_expresso.menu.Coffee
import com.tamayo_aaron_b.cupfe_expresso.reservation.ReservationConnect
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class OTPRequests(val email: String)


data class ResetPasswordRequest(val email: String, val otp: String, val new_password: String)
data class ResetPasswordResponse(val success: Boolean, val message: String)
data class ReservationResponse(
    val message: String,
    @SerializedName("reference_number") val transactionId: String
)


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

    @POST("api/table_reservations")
    fun reserveTables(@Body reservation: ReservationConnect): Call<ReservationResponse>

    @GET("api/items")
    fun getCoffees(): Call<List<Coffee>>

    @GET("api/items/category")
    fun getAllCoffees(@Query("category") category: String = "Coffee"): Call<List<Coffee>>

    @GET("api/items/category")
    fun getAllFoods(@Query("category") category: String = "Food"): Call<List<Coffee>>

    @GET("api/items/search")
    fun searchCoffees(@Query("search") query: String): Call<List<Coffee>>

}