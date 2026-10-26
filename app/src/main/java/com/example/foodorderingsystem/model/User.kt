package com.example.foodorderingsystem.model

data class User(
    val username: String,
    val email: String,
    val phone: String = "+63 917 123 4567",
    val address: String = "123 Foodie Street, Metro Manila"
)
