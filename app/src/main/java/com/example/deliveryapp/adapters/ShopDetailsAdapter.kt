package com.example.deliveryapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliveryapp.Dishes.ShopDishes
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.DishItemBinding

class ShopDetailsAdapter(private val dishes: MutableList<ShopDishes>): RecyclerView.Adapter<ShopDetailsAdapter.ShopDetailsViewHolder>() {

    inner class ShopDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = DishItemBinding.bind(itemView)
        fun bindData(dish: ShopDishes){
            binding.dishTitle.text = dish.title
            binding.dishPrice.text = dish.price
            Glide.with(itemView)
                .load(dish.imageUrl)
                .into(binding.ivDishImg)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateDishes(newDishes: MutableList<ShopDishes>) {
        dishes.clear()
        dishes.addAll(newDishes)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        return ShopDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    override fun onBindViewHolder(holder: ShopDetailsViewHolder, position: Int) {
        val dish = dishes[position]
        holder.bindData(dish)
    }
}