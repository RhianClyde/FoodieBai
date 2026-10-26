package com.example.foodorderingsystem.model

data class FoodItem(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val imageResId: Int,
    val backgroundResId: Int,
    var isFavorite: Boolean = false
)
