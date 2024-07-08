package com.example.deliveryapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.models.OrderItem

class OrderItemsAdapter(private val items: List<OrderItem>) : RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder>() {

    class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
        val itemPrice: TextView = itemView.findViewById(R.id.tv_item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemName.text = currentItem.productTitle
        holder.itemPrice.text = String.format("₹%.2f", currentItem.productPrice)
    }

    override fun getItemCount() = items.size
}