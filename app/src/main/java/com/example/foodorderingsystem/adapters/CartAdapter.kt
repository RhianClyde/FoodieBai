package com.example.foodorderingsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.data.CartManager
import com.example.foodorderingsystem.model.CartItem

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onItemChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoodImage: ImageView = itemView.findViewById(R.id.iv_food_image)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvUnitPrice: TextView = itemView.findViewById(R.id.tv_unit_price)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tv_total_price)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val btnMinus: ImageView = itemView.findViewById(R.id.btn_minus)
        val btnPlus: ImageView = itemView.findViewById(R.id.btn_plus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val food = cartItem.foodItem

        holder.ivFoodImage.setImageResource(food.imageResId)
        holder.tvTitle.text = food.title
        holder.tvUnitPrice.text = "₱${food.price} each"
        holder.tvTotalPrice.text = "₱${food.price * cartItem.quantity}"
        holder.tvQuantity.text = cartItem.quantity.toString()

        // UI UPDATE v2: Plus button tactile scale animation
        holder.btnPlus.setOnClickListener {
            holder.btnPlus.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                holder.btnPlus.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                CartManager.updateQuantity(food, cartItem.quantity + 1)
                updateData(CartManager.getCartItems())
                onItemChanged()
            }.start()
        }

        // UI UPDATE v2: Minus button tactile scale animation
        holder.btnMinus.setOnClickListener {
            holder.btnMinus.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                holder.btnMinus.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                CartManager.updateQuantity(food, cartItem.quantity - 1)
                updateData(CartManager.getCartItems())
                onItemChanged()
            }.start()
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newList: List<CartItem>) {
        cartItems = newList
        notifyDataSetChanged()
    }
}
