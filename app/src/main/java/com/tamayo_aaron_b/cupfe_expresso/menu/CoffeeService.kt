package com.tamayo_aaron_b.cupfe_expresso.menu

import retrofit2.Call
import retrofit2.http.GET

interface CoffeeService {
    @GET("api/items")
    fun getCoffees(): Call<List<Coffee>>
}