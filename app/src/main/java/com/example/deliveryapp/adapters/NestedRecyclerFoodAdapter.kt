package com.example.deliveryapp.adapters

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FoodItemBinding
import com.example.deliveryapp.models.NestedRecyclerModelFood

class NestedRecyclerFoodAdapter(
    private val foodItems: List<NestedRecyclerModelFood>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<NestedRecyclerFoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int = foodItems.size

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.bind(foodItem)
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FoodItemBinding.bind(itemView)
        fun bind(foodItem: NestedRecyclerModelFood) {
            binding.apply {
                imageFoodPoster.load(foodItem.imageUrl)
                textShopName.text = foodItem.shopName
                imageFoodPoster.setGrayscale(!foodItem.status)
            }
            itemView.setOnClickListener {
                onItemClick(foodItem.shopName)
            }
        }
    }
}

private fun ImageView.setGrayscale(isGrayscale: Boolean) {
    if (isGrayscale) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        this.colorFilter = filter
    } else {
        this.clearColorFilter()
    }
}
