package com.example.foodorderingsystem.data

import com.example.foodorderingsystem.model.CartItem
import com.example.foodorderingsystem.model.FoodItem
import com.example.foodorderingsystem.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CartManager {
    private val cartItems = mutableListOf<CartItem>()
    private val activeOrders = mutableListOf<Order>()
    val deliveryFee = 49

    fun addToCart(foodItem: FoodItem) {
        val existing = cartItems.find { it.foodItem.id == foodItem.id }
        if (existing != null) {
            existing.quantity++
        } else {
            cartItems.add(CartItem(foodItem, 1))
        }
    }

    fun removeFromCart(foodItem: FoodItem) {
        cartItems.removeAll { it.foodItem.id == foodItem.id }
    }

    fun updateQuantity(foodItem: FoodItem, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(foodItem)
        } else {
            val existing = cartItems.find { it.foodItem.id == foodItem.id }
            existing?.quantity = quantity
        }
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()

    fun getCartCount(): Int = cartItems.sumOf { it.quantity }

    fun getSubtotal(): Int = cartItems.sumOf { it.foodItem.price * it.quantity }

    fun getTotalPrice(): Int {
        val sub = getSubtotal()
        return if (sub > 0) sub + deliveryFee else 0
    }

    fun placeOrder(): Order? {
        if (cartItems.isEmpty()) return null
        val timeStamp = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date())
        val order = Order(
            orderId = "ORD-" + System.currentTimeMillis().toString().takeLast(6),
            items = cartItems.toList(),
            totalPrice = getTotalPrice(),
            orderTime = timeStamp,
            status = "Preparing"
        )
        activeOrders.add(0, order)
        cartItems.clear()
        return order
    }

    fun getOrders(): List<Order> = activeOrders.toList()
}
