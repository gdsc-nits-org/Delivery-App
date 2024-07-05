package com.example.deliveryapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ParentItemBinding
import com.example.deliveryapp.models.NestedRecyclerModelMain

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
            rvMovieChild.adapter = NestedRecyclerFoodAdapter(collection.movieModel) { shop ->
                val message = "Shop: ${shop.shopName}\n" + "Status: ${shop.status}"
                Toast.makeText(holder.itemView.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

//    fun updateData(newCollections: List<NestedRecyclerModelMain>) {
//        collections = newCollections
//        notifyDataSetChanged()
//    }
@SuppressLint("NotifyDataSetChanged")
fun updateData(newCollections: List<NestedRecyclerModelMain>) {
        collections = newCollections
        notifyDataSetChanged()
    }

    inner class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ParentItemBinding.bind(itemView)
    }
}
