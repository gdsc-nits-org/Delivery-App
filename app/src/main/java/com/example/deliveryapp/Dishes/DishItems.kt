package com.example.deliveryapp.Dishes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R


class DishItems : Fragment() {

    private var recyclerView:RecyclerView?=null
    private var recyclerDishAdapter:DishAdapter?=null
    private var dishList= mutableListOf<Dish>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dish_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dishList=ArrayList()
        recyclerView=view.findViewById(R.id.rvDish) as RecyclerView
        recyclerDishAdapter= DishAdapter(this@DishItems,dishList)
        val layoutManager:RecyclerView.LayoutManager=GridLayoutManager(context,2)
        recyclerView!!.layoutManager=layoutManager
        recyclerView!!.adapter=recyclerDishAdapter

        prepareDishList()
    }

    private fun prepareDishList() {
        var dish=Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50")
        dishList.add(dish)
        dish=Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50")
        dishList.add(dish)
        dish=Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50")
        dishList.add(dish)
        dish=Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50")
        dishList.add(dish)
        dish=Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50")
        dishList.add(dish)
        dish=Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50")
        dishList.add(dish)

        recyclerDishAdapter!!.notifyDataSetChanged()
    }

}