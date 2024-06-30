package com.example.deliveryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.models.NestedRecyclerModelMain
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ParentItemBinding

class NestedRecyclerAdapter(private var collections: List<NestedRecyclerModelMain>) :
    RecyclerView.Adapter<NestedRecyclerAdapter.CollectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_item, parent, false)
        return CollectionViewHolder(view)
    }

    override fun getItemCount(): Int = collections.size

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = collections[position]
        holder.binding.apply {
            Genere.text = collection.title
            rvMovieChild.adapter = NestedRecyclerFoodAdapter(collection.movieModel)
        }
    }

    fun updateData(newCollections: List<NestedRecyclerModelMain>) {
        collections = newCollections
        notifyDataSetChanged()
    }

    inner class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ParentItemBinding.bind(itemView)
    }
}
