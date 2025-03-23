package com.tamayo_aaron_b.cupfe_expresso.menu

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object CoffeeClient {
    private const val BASE_URL="http://192.168.1.16/expresso-cafe/" // Replace with your PHP backend URL


    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Increase write timeout
            .build() // Build OkHttpClient after setting timeouts
    }

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: CoffeeService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CoffeeService::class.java)
    }
}