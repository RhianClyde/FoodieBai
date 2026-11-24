package com.example.foodorderingsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.data.CartManager
import com.example.foodorderingsystem.data.FoodRepository
import com.example.foodorderingsystem.model.FoodItem

class FoodAdapter(
    private var foodList: List<FoodItem>,
    private val onAddToCartClick: ((FoodItem) -> Unit)? = null
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flImageContainer: FrameLayout = itemView.findViewById(R.id.fl_image_container)
        val tvRating: TextView = itemView.findViewById(R.id.tv_rating)
        val btnFavorite: ImageView = itemView.findViewById(R.id.btn_favorite)
        val ivFoodImage: ImageView = itemView.findViewById(R.id.iv_food_image)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val btnAddToCart: ImageView = itemView.findViewById(R.id.btn_add_to_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_card, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val item = foodList[position]

        holder.flImageContainer.setBackgroundResource(item.backgroundResId)
        holder.tvRating.text = item.rating.toString()
        holder.ivFoodImage.setImageResource(item.imageResId)
        holder.tvTitle.text = item.title
        holder.tvCategory.text = item.category
        holder.tvDescription.text = item.description
        holder.tvPrice.text = "₱${item.price}"

        // Favorite icon state
        updateFavoriteIcon(holder.btnFavorite, item.isFavorite)

        // UI UPDATE v2: Heart bounce micro-interaction on toggle
        holder.btnFavorite.setOnClickListener {
            holder.btnFavorite.animate().scaleX(1.3f).scaleY(1.3f).setDuration(120).withEndAction {
                val isFav = FoodRepository.toggleFavorite(item.id)
                updateFavoriteIcon(holder.btnFavorite, isFav)
                holder.btnFavorite.animate().scaleX(1.0f).scaleY(1.0f).setDuration(120).start()
            }.start()
        }

        // UI UPDATE v2: Add to cart button scale micro-interaction
        holder.btnAddToCart.setOnClickListener {
            holder.btnAddToCart.animate().scaleX(0.85f).scaleY(0.85f).setDuration(90).withEndAction {
                holder.btnAddToCart.animate().scaleX(1.0f).scaleY(1.0f).setDuration(90).start()
                CartManager.addToCart(item)
                onAddToCartClick?.invoke(item)
            }.start()
        }
    }

    private fun updateFavoriteIcon(imageView: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.ic_heart_filled)
        } else {
            imageView.setImageResource(R.drawable.ic_heart_outline)
        }
    }

    override fun getItemCount(): Int = foodList.size

    fun updateData(newList: List<FoodItem>) {
        foodList = newList
        notifyDataSetChanged()
    }
}
