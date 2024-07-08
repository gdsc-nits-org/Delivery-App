package com.example.deliveryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.deliveryapp.Dishes.Dish
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ForYouBinding

class ShopForYouAdapter(private val list : MutableList<Dish>): RecyclerView.Adapter<ShopForYouAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ForYouBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.for_you, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            dishImage.load(item.image)
            tvBiryani.text = item.title
            tvBiryaniHub.text = item.resName
        }
    }
}