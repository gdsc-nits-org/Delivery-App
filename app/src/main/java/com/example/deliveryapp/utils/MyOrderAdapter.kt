package com.example.deliveryapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R



data class Order(
    val storeName: String,
    val location: String,
    val price: String,
    val productName: String,
    val dateTime: String,
    val deliveryStatus: String
)

class MyOrdersAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>() {

    class MyOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.tv_store_name)
        val location: TextView = itemView.findViewById(R.id.tv_location)
        val price: TextView = itemView.findViewById(R.id.tv_price)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val dateTime: TextView = itemView.findViewById(R.id.tv_date_time)
        val deliveryStatus: TextView = itemView.findViewById(R.id.tv_delivery_status)
        val reorderButton: Button = itemView.findViewById(R.id.btn_Reorder)
        val rateOrderButton: Button = itemView.findViewById(R.id.btn_Rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_orders_r_v, parent, false)
        return MyOrdersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val currentOrder = orderList[position]
        holder.storeName.text = currentOrder.storeName
        holder.location.text = currentOrder.location
        holder.price.text = currentOrder.price
        holder.productName.text = currentOrder.productName
        holder.dateTime.text = currentOrder.dateTime
        holder.deliveryStatus.text = currentOrder.deliveryStatus


        holder.reorderButton.setOnClickListener {

        }
        holder.rateOrderButton.setOnClickListener {

        }
    }

    override fun getItemCount() = orderList.size
}
