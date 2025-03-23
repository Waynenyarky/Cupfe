package com.tamayo_aaron_b.cupfe_expresso

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService1 {
    @Headers("Content-Type: application/json")
    @POST("api/users/login") // Ensure correct API endpoint
    fun verifyOTPSignIn(@Body request: OTPRequest): Call<ApiResponse>
}