package com.example.foodorderingsystem.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.adapters.OrdersAdapter
import com.example.foodorderingsystem.data.CartManager

class OrdersFragment : Fragment() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var tvEmptyOrders: TextView
    private lateinit var adapter: OrdersAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var refreshRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        rvOrders = view.findViewById(R.id.rv_orders)
        tvEmptyOrders = view.findViewById(R.id.tv_empty_orders)

        adapter = OrdersAdapter(CartManager.getOrders())
        rvOrders.layoutManager = LinearLayoutManager(context)
        rvOrders.adapter = adapter

        updateOrdersList()

        return view
    }

    private fun updateOrdersList() {
        val orders = CartManager.getOrders()
        if (orders.isEmpty()) {
            tvEmptyOrders.visibility = View.VISIBLE
            rvOrders.visibility = View.GONE
        } else {
            tvEmptyOrders.visibility = View.GONE
            rvOrders.visibility = View.VISIBLE
            adapter.updateData(orders)
        }
    }

    private fun startLiveRefresh() {
        refreshRunnable = object : Runnable {
            override fun run() {
                updateOrdersList()
                handler.postDelayed(this, 2000) // Refresh UI every 2 seconds
            }
        }
        handler.post(refreshRunnable!!)
    }

    private fun stopLiveRefresh() {
        refreshRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onResume() {
        super.onResume()
        updateOrdersList()
        startLiveRefresh()
    }

    override fun onPause() {
        super.onPause()
        stopLiveRefresh()
    }
}
