package com.tamayo_aaron_b.cupfe_expresso

import com.google.gson.JsonObject

data class ApiResponse(
    val message: String? = null,
    val token: String,
    val username: String,
    val data: JsonObject? = null,  // Use JsonObject if data structure is unknown
    val error: Boolean = false,
    val status: String = "",
    val key1: String = "",
    val key2: Int = 0
)