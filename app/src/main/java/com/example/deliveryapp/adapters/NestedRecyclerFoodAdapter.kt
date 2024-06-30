package com.example.deliveryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FoodItemBinding

class NestedRecyclerFoodAdapter(
    private val foodItems: List<NestedRecyclerModelFood>,
    private val onItemClick: (NestedRecyclerModelFood) -> Unit
) : RecyclerView.Adapter<NestedRecyclerFoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int = foodItems.size

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val shop = foodItems[position]
        holder.binding.apply {
            imageFoodPoster.load(shop.imageUrl)
            // You can add more UI elements here if needed, e.g., shop name
            root.setOnClickListener { onItemClick(shop) }
        }
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FoodItemBinding.bind(itemView)
    }
}