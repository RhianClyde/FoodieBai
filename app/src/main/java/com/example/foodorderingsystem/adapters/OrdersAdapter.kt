package com.example.foodorderingsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.model.Order

class OrdersAdapter(
    private var orders: List<Order>
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tv_order_id)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvItemsSummary: TextView = itemView.findViewById(R.id.tv_items_summary)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tv_total_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.tvOrderId.text = order.orderId
        holder.tvStatus.text = order.status
        holder.tvTime.text = order.orderTime
        holder.tvTotalPrice.text = "₱${order.totalPrice}"

        val summary = order.items.joinToString(", ") { "${it.quantity}x ${it.foodItem.title}" }
        holder.tvItemsSummary.text = summary
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }
}
