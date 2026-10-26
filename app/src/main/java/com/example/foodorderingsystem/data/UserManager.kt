package com.example.foodorderingsystem.data

import com.example.foodorderingsystem.model.User

object UserManager {
    var currentUser: User? = User("FoodieUser", "user@foodiebai.com")

    fun login(username: String, email: String) {
        currentUser = User(
            username = username.ifBlank { "FoodieUser" },
            email = email.ifBlank { "user@foodiebai.com" }
        )
    }

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean = currentUser != null
}
