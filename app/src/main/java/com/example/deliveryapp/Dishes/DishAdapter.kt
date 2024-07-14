package com.example.deliveryapp.Dishes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R

class DishAdapter(private val dishList: List<Dish>):
    RecyclerView.Adapter<DishAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dishTitle.text = dishList[position].title
        holder.dishImg.setImageResource(dishList[position].image)
        holder.addImg.setImageResource(R.drawable.baseline_add_circle_24)
        holder.resName.text = dishList[position].resName
        holder.dishPrice.text = dishList[position].price
        holder.rupees.text = "Rs"
        holder.cardView.setOnClickListener {
            Toast.makeText(holder.itemView.context,dishList[position].title,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val dishTitle:TextView = itemView.findViewById(R.id.dish_title)
        val dishImg: ImageView = itemView.findViewById(R.id.ivDishImg)
        val resName:TextView = itemView.findViewById(R.id.res_name)
        val addImg:ImageView = itemView.findViewById(R.id.addItem)
        val dishPrice:TextView = itemView.findViewById(R.id.dish_price)
        val rupees:TextView = itemView.findViewById(R.id.price_currency)
        val cardView:CardView = itemView.findViewById(R.id.cardView)
    }
}