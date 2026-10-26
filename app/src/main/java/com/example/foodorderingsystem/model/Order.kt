package com.example.foodorderingsystem.model

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val totalPrice: Int,
    val orderTime: String,
    val status: String // e.g. "Preparing", "On the way", "Delivered"
)
