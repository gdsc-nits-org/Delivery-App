package com.example.deliveryapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.models.Order

class MyOrdersAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>() {

    class MyOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.tv_store_name)
        val deliveryStatus: TextView = itemView.findViewById(R.id.tv_delivery_status)
        val dateTime: TextView = itemView.findViewById(R.id.tv_date_time)
        val orderItemsRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_order_items)
        val totalPrice: TextView = itemView.findViewById(R.id.tv_total_price)
        val reorderButton: Button = itemView.findViewById(R.id.btn_reorder)
        val rateOrderButton: Button = itemView.findViewById(R.id.btn_rate_order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_orders_r_v, parent, false)
        return MyOrdersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val currentOrder = orderList[position]
        holder.storeName.text = currentOrder.storeName
        holder.deliveryStatus.text = if (currentOrder.deliveryStatus == "true") "Delivered" else "Pending"
        holder.dateTime.text = currentOrder.dateTime

        holder.orderItemsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.orderItemsRecyclerView.adapter = OrderItemsAdapter(currentOrder.items)

        val totalPrice = currentOrder.items.sumByDouble { it.productPrice }
        holder.totalPrice.text = String.format("₹%.2f", totalPrice)

        holder.reorderButton.setOnClickListener {

        }

        holder.rateOrderButton.setOnClickListener {

        }
    }

    override fun getItemCount() = orderList.size
}