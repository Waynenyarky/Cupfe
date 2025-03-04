package com.tamayo_aaron_b.cupfe_expresso

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val message: String? = null,
    val data: JsonObject? = null,  // Use JsonObject if data structure is unknown
    val error: Boolean = false,
    val status: String = "",
    val key1: String = "",
    val key2: Int = 0,
    val user: UserData?,
    val rawResponse: String? = null
)

data class UserData(val id: Int, val name: String, val email: String)

interface ApiService1 {
    @Headers("Content-Type: application/json")
    @POST("api/users/login") // Ensure correct API endpoint
    fun sendOTP(@Body request: LoginRequest): Call<ResponseBody>

    @POST("api/users/verify-login-otp")
    fun verifyOTPSignIn(@Body request: VerifyOTPRequest): Call<ApiResponse>
}