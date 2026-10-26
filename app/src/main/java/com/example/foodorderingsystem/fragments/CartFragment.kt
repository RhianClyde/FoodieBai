package com.example.foodorderingsystem.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.MainActivity
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.adapters.CartAdapter
import com.example.foodorderingsystem.data.CartManager

class CartFragment : Fragment() {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDeliveryFee: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvEmptyCart: TextView
    private lateinit var btnCheckout: Button
    private lateinit var cardSummary: View
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        rvCartItems = view.findViewById(R.id.rv_cart_items)
        tvSubtotal = view.findViewById(R.id.tv_subtotal)
        tvDeliveryFee = view.findViewById(R.id.tv_delivery_fee)
        tvTotalPrice = view.findViewById(R.id.tv_total_price)
        tvEmptyCart = view.findViewById(R.id.tv_empty_cart)
        btnCheckout = view.findViewById(R.id.btn_checkout)
        cardSummary = view.findViewById(R.id.card_summary)

        adapter = CartAdapter(CartManager.getCartItems()) {
            updateCartUI()
        }
        rvCartItems.layoutManager = LinearLayoutManager(context)
        rvCartItems.adapter = adapter

        btnCheckout.setOnClickListener {
            val order = CartManager.placeOrder()
            if (order != null) {
                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_LONG).show()
                (activity as? MainActivity)?.selectBottomTab(R.id.nav_orders)
            } else {
                Toast.makeText(context, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            }
        }

        updateCartUI()

        return view
    }

    private fun updateCartUI() {
        val items = CartManager.getCartItems()
        if (items.isEmpty()) {
            tvEmptyCart.visibility = View.VISIBLE
            rvCartItems.visibility = View.GONE
            cardSummary.visibility = View.GONE
        } else {
            tvEmptyCart.visibility = View.GONE
            rvCartItems.visibility = View.VISIBLE
            cardSummary.visibility = View.VISIBLE
            adapter.updateData(items)

            val subtotal = CartManager.getSubtotal()
            tvSubtotal.text = "₱$subtotal"
            tvDeliveryFee.text = "₱${CartManager.deliveryFee}"
            tvTotalPrice.text = "₱${CartManager.getTotalPrice()}"
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartUI()
    }
}
