package com.example.deliveryapp.adapters

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.Transformation
import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FoodItemBinding
import android.graphics.Paint
import android.graphics.Canvas

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
            imageFoodPoster.load(shop.imageUrl) {
                if (!shop.isOpen) {
                    transformations(GrayscaleTransformation())
                }
            }
            // You can add more UI elements here if needed, e.g., shop name
            root.setOnClickListener { onItemClick(shop) }
        }
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FoodItemBinding.bind(itemView)
    }
}

class GrayscaleTransformation : Transformation {
    override val cacheKey: String = "grayscaleTransformation"

    override suspend fun transform(input: Bitmap, size: coil.size.Size): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, input.config)
        val canvas = Canvas(output)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(input, 0f, 0f, paint)
        return output
    }
}