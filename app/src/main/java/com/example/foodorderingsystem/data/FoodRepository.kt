package com.example.foodorderingsystem.data

import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.model.FoodItem

object FoodRepository {
    private val foodItemsList = mutableListOf(
        FoodItem(
            id = "1",
            title = "Korean Gochujang Chicken",
            category = "CHICKEN",
            description = "Crispy chicken tossed in sweet, spicy Korean glaze.",
            price = 175,
            rating = 4.8,
            imageResId = R.drawable.ic_food_chicken,
            backgroundResId = R.drawable.bg_food_card_1,
            isFavorite = true
        ),
        FoodItem(
            id = "2",
            title = "Grilled Steak",
            category = "STEAK",
            description = "Tender flame-grilled steak with herb butter and pan jus.",
            price = 255,
            rating = 4.8,
            imageResId = R.drawable.ic_food_steak,
            backgroundResId = R.drawable.bg_food_card_2
        ),
        FoodItem(
            id = "3",
            title = "Margherita Pizza",
            category = "PIZZA",
            description = "Stone-baked crust, bright tomato, mozzarella, and basil.",
            price = 229,
            rating = 4.8,
            imageResId = R.drawable.ic_food_pizza,
            backgroundResId = R.drawable.bg_food_card_3
        ),
        FoodItem(
            id = "4",
            title = "Mango Crepe Cake",
            category = "CAKE",
            description = "Paper-thin crepes layered with mango and vanilla.",
            price = 125,
            rating = 4.8,
            imageResId = R.drawable.ic_food_cake,
            backgroundResId = R.drawable.bg_food_card_4
        ),
        FoodItem(
            id = "5",
            title = "Truffle Smash Burger",
            category = "BURGER",
            description = "Double beef patty, truffle mayo, aged cheddar.",
            price = 199,
            rating = 4.9,
            imageResId = R.drawable.ic_food_burger,
            backgroundResId = R.drawable.bg_food_card_1
        ),
        FoodItem(
            id = "6",
            title = "Matcha Milk Tea",
            category = "MILK TEA",
            description = "Rich Japanese matcha with chewy tapioca pearls.",
            price = 145,
            rating = 4.7,
            imageResId = R.drawable.ic_food_milktea,
            backgroundResId = R.drawable.bg_food_card_5
        )
    )

    fun getAllFoods(): List<FoodItem> = foodItemsList

    fun getFoodsByCategory(category: String): List<FoodItem> {
        if (category.equals("All", ignoreCase = true)) return foodItemsList
        return foodItemsList.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun searchFoods(query: String): List<FoodItem> {
        if (query.isBlank()) return foodItemsList
        return foodItemsList.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }

    fun toggleFavorite(foodId: String): Boolean {
        val item = foodItemsList.find { it.id == foodId }
        item?.let {
            it.isFavorite = !it.isFavorite
            return it.isFavorite
        }
        return false
    }

    fun getFavoriteFoods(): List<FoodItem> {
        return foodItemsList.filter { it.isFavorite }
    }
}
