package com.example.foodorderingsystem.data

import android.os.Handler
import android.os.Looper
import com.example.foodorderingsystem.model.CartItem
import com.example.foodorderingsystem.model.FoodItem
import com.example.foodorderingsystem.model.Order
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    fun placeOrder(orderId: String): Order? {
        if (cartItems.isEmpty()) return null
        val timeStamp = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date())
        
        val order = Order(
            orderId = orderId,
            items = cartItems.toList(),
            totalPrice = getTotalPrice(),
            orderTime = timeStamp,
            status = "Pending"
        )
        activeOrders.add(0, order)
        cartItems.clear()

        // AUTOMATIC STATUS PROGRESSION & FIREBASE SYNC ON THE SAME DOCUMENT (orders/{orderId})
        val handler = Handler(Looper.getMainLooper())

        // 15 Seconds -> Preparing 🍳
        handler.postDelayed({
            order.status = "Preparing 🍳"
            try {
                FirebaseFirestore.getInstance().collection("orders")
                    .document(orderId)
                    .update("status", "Preparing 🍳")
            } catch (e: Exception) {}
        }, 15000)

        // 35 Seconds -> On the way 🛵
        handler.postDelayed({
            order.status = "On the way 🛵"
            try {
                FirebaseFirestore.getInstance().collection("orders")
                    .document(orderId)
                    .update("status", "On the way 🛵")
            } catch (e: Exception) {}
        }, 35000)

        // 55 Seconds -> Delivered ✅
        handler.postDelayed({
            order.status = "Delivered ✅"
            try {
                FirebaseFirestore.getInstance().collection("orders")
                    .document(orderId)
                    .update(mapOf(
                        "status" to "Delivered ✅",
                        "deliveredAt" to FieldValue.serverTimestamp()
                    ))
            } catch (e: Exception) {}
        }, 55000)

        return order
    }

    fun getOrders(): List<Order> = activeOrders.toList()
}
