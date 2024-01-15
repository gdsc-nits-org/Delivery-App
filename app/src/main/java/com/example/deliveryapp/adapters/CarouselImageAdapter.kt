package com.example.deliveryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliveryapp.models.CarouselImageItem
import com.example.deliveryapp.R

class ImageAdapter : ListAdapter<CarouselImageItem,ImageAdapter.ViewHolder>(DiffCallback()){

    class DiffCallback : DiffUtil.ItemCallback<CarouselImageItem>(){
        override fun areItemsTheSame(oldItem: CarouselImageItem, newItem: CarouselImageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarouselImageItem, newItem: CarouselImageItem): Boolean {
            return oldItem == newItem
        }

    }
    class ViewHolder(iteView: View): RecyclerView.ViewHolder(iteView){
        private val imageView = iteView.findViewById<ImageView>(R.id.imageView)


        fun bindData(item: CarouselImageItem){
            Glide.with(itemView)
                .load(item.url)
                .into(imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = getItem(position)
        holder.bindData(imageItem)
    }
}