package com.example.foodorderingsystem.data

import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.model.FoodItem

object FoodRepository {
    private val foodItemsList = mutableListOf(
        FoodItem(
            id = "1",
                  title = "8 - pc. Chickenjoy Solo",
            category = "Jollibee",
            description = "Eight pieces of crispy, juicy, flavorful, golden-fried Chickenjoy.",
            price = 748,
            rating = 4.8,
            imageResId = R.drawable.jollibee_chickenjoy_bucket,
            backgroundResId = R.drawable.bg_jollibee,
            isFavorite = true
        ),
        FoodItem(
            id = "2",
            title = "Fries",
            category = "McDonald's",
            description = "Crispy, golden, salted French fries with a delicious potato flavor.",
            price = 83,
            rating = 4.8,
            imageResId = R.drawable.mcdo_fries,
            backgroundResId = R.drawable.bg_mcdonalds
        ),
        FoodItem(
            id = "3",
            title = "Famous Bowl Fully Loaded Meal Double Rice Deal",
            category = "KFC",
            description = "A crispy fried chicken piece paired with two servings of rice and flavorful gravy.",
            price = 225,
            rating = 4.8,
            imageResId = R.drawable.kfc_famous_bowl_fully_loaded,
            backgroundResId = R.drawable.bg_kfc
        ),
        FoodItem(
            id = "4",
            title = "Siomai Chao Fan",
            category = "Chowking",
            description = "Savory fried rice topped with tender, flavorful siomai dumplings.",
            price = 116,
            rating = 4.8,
            imageResId = R.drawable.chowking_siomai_chao_fan,
            backgroundResId = R.drawable.bg_chowking
        ),
        FoodItem(
            id = "5",
            title = "Truffle Smash Burger",
            category = "Jollibee",
            description = "Double beef patty, truffle mayo, aged cheddar.",
            price = 109,
            rating = 4.9,
            imageResId = R.drawable.burger_image,
            backgroundResId = R.drawable.bg_jollibee
        ),
        FoodItem(
            id = "6",
            title = "Matcha Milk Tea",
            category = "Starbucks",
            description = "Rich Japanese matcha with chewy tapioca pearls.",
            price = 50,
            rating = 4.7,
            imageResId = R.drawable.matcha_image,
            backgroundResId = R.drawable.bg_starbucks
        ),
        FoodItem(
            id = "7",
            title = "Roasted Macadamia Oatmilk Frappuccino",
            category = "Starbucks",
            description = "nutty blended coffee made with oatmilk.",
            price = 115,
            rating = 4.5,
            imageResId = R.drawable.starbucks_roasted_macadamia_oatmilk,
            backgroundResId = R.drawable.bg_starbucks
        ),
        FoodItem(
            id = "8",
            title = "Creamy Halo-Halo",
            category = "Mang inasal",
            description = "Shaved ice dessert with sweet toppings and milk.",
            price = 150,
            rating = 4.8,
            imageResId = R.drawable.mang_inasal_halo_halo,
            backgroundResId = R.drawable.bg_mang_inasal
        ),
        FoodItem(
            id = "9",
            title = "Iced Cappuccino",
            category = "Starbucks",
            description = "Chilled espresso topped with thick, frothy milk.",
            price = 105,
            rating = 4.6,
            imageResId = R.drawable.starbucks_iced_cappuccino,
            backgroundResId = R.drawable.bg_starbucks
        ),
        FoodItem(
            id = "10",
            title = "Sweet 'N' Sour Pork",
            category = "Chowking",
            description = "Crispy pork bites tossed in tangy-sweet sauce.",
            price = 155,
            rating = 4.6,
            imageResId = R.drawable.chowking_sweet_n_sour,
            backgroundResId = R.drawable.bg_chowking
        ),
        FoodItem(
            id = "11",
            title = "Paa Large",
            category = "Mang inasal",
            description = "Juicy grilled chicken leg with unlimited rice.",
            price = 139,
            rating = 4.9,
            imageResId = R.drawable.mang_inasal_paa_large,
            backgroundResId = R.drawable.bg_mang_inasal
        ),
        FoodItem(
            id = "12",
            title = "Pecho Large",
            category = "Mang inasal",
            description = "Smoky grilled chicken breast served with rice.",
            price = 169,
            rating = 4.8,
            imageResId = R.drawable.mang_inasal_pecho_large,
            backgroundResId = R.drawable.bg_mang_inasal
        ),
        FoodItem(
            id = "13",
            title = "McFlurry with Oreo Cookies",
            category = "McDonald's",
            description = "Creamy soft serve blended with candy toppings.",
            price = 169,
            rating = 4.3,
            imageResId = R.drawable.mcdo_mcflurry,
            backgroundResId = R.drawable.bg_mcdonalds
        ),
        FoodItem(
            id = "14",
            title = "Choco Lava Puff",
            category = "McDonald's",
            description = "Warm crispy pastry with molten chocolate filling.",
            price = 59,
            rating = 4.5,
            imageResId = R.drawable.mcdo_choco_lava,
            backgroundResId = R.drawable.bg_mcdonalds
        ),
        FoodItem(
            id = "15",
            title = "Palabok Regular Size",
            category = "Mang inasal",
            description = "Rice noodles in shrimp sauce with chicharon.",
            price = 89,
            rating = 4.3,
            imageResId = R.drawable.mang_inasal_palabok,
            backgroundResId = R.drawable.bg_mang_inasal
        ),
        FoodItem(
            id = "16",
            title = "Coke McFloat",
            category = "McDonald's",
            description = "Fizzy Coke topped with vanilla soft serve.",
            price = 57,
            rating = 4.5,
            imageResId = R.drawable.mcdo_coke_mcfloat,
            backgroundResId = R.drawable.bg_mcdonalds
        ),
        FoodItem(
            id = "17",
            title = "Pancit Canton",
            category = "Chowking",
            description = "Savory stir-fried noodles with veggies and chicken.",
            price = 56,
            rating = 4.0,
            imageResId = R.drawable.chowking_pancit,
            backgroundResId = R.drawable.bg_chowking
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
