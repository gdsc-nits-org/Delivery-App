package com.example.deliveryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FoodItemBinding

class NestedRecyclerFoodAdapter (private val movieModel: List<NestedRecyclerModelFood>) :
    RecyclerView.Adapter<NestedRecyclerFoodAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieModel.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.binding.apply {
            imageFoodPoster.load(movieModel[position].imageUrl)
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Toast.makeText(context, "Image URL: ${movieModel[position].imageUrl}", Toast.LENGTH_SHORT).show()
        }
    }

    inner class MovieViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = FoodItemBinding.bind(itemView)
    }
}