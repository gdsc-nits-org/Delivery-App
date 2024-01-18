package com.example.deliveryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.models.NestedRecyclerModelMain
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ParentItemBinding

class NestedRecyclerAdapter(private val collection : List<NestedRecyclerModelMain>) :
    RecyclerView.Adapter<NestedRecyclerAdapter.CollectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_item, parent, false)
        return CollectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.binding.apply {
            val collection = collection[position]
            Genere.text = collection.title
            val foodAdapter = NestedRecyclerFoodAdapter(collection.movieModel)
            rvMovieChild.adapter = foodAdapter
        }
    }

    inner class CollectionViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ParentItemBinding.bind(itemView)
    }
}