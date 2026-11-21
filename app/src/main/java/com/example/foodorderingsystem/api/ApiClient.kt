package com.example.foodorderingsystem.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // 10.0.2.2 points to localhost on your dev PC from the Android Emulator
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val apiService: FoodApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodApiService::class.java)
    }
}
