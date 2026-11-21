package com.example.foodorderingsystem.api

import com.example.foodorderingsystem.model.FoodItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginRequest(val identifier: String, val password: String)
data class AuthResponse(
    val message: String?,
    val userId: String?,
    val username: String?,
    val email: String?,
    val error: String?
)

interface FoodApiService {
    @GET("api/foods")
    suspend fun getFoods(): Response<List<FoodItem>>

    @POST("api/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>
}
