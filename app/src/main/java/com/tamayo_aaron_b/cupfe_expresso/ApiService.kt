package com.tamayo_aaron_b.cupfe_expresso

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/create")
    fun sendOTP(@Body request: OTPRequest): Call<String>

    @POST("api/users/verify-otp")
    fun verifyOTP(@Body request: VerifyOTPRequest): Call<ApiResponse>
}