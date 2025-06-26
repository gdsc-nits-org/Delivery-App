package com.example.deliveryapp.Dishes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
//import androidx.preference.contains
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import java.util.ArrayList
import kotlin.collections.addAll
import kotlin.text.clear
import kotlin.text.lowercase


class DishItems : Fragment() {
     private lateinit var dishAdapter: DishAdapter
     private var allDishesList: MutableList<Dish> = mutableListOf()
     private var displayedDishesList: MutableList<Dish> = mutableListOf()
     private lateinit var rvDishes: RecyclerView
//    private var recyclerView:RecyclerView?=null
//    private var recyclerDishAdapter:DishAdapter?=null
//    private var dishList= mutableListOf<Dish>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dish_items, container, false)
       if(allDishesList.isEmpty()){
        prepareDishList()

    }
    displayedDishesList.clear()
    displayedDishesList.addAll(allDishesList)
    return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDishes=view.findViewById(R.id.rvDish)
        rvDishes.layoutManager = GridLayoutManager(context, 2)
        dishAdapter= DishAdapter(displayedDishesList)
        rvDishes.adapter= dishAdapter

        val initialQuery = arguments?.getString("initial_search_query") ?: ""

        filterDishes(initialQuery)
    }

    private fun prepareDishList() {
        allDishesList.clear()
        allDishesList.add(Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50"))

        allDishesList.add(Dish("Veg momo",R.drawable.barbeque,"NITS CAFE","50"))
//        dishList.add(dish)
        allDishesList.add(Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50"))
//        dishList.add(dish)
        allDishesList.add(Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50"))
        allDishesList.add(Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50"))
        allDishesList.add(Dish("Barbeque",R.drawable.barbeque,"NITS CAFE","50"))

//        recyclerDishAdapter!!.notifyDataSetChanged()
    }
    fun filterDishes(query: String) {

        displayedDishesList.clear()
        val safeQuery = query ?: ""

        if (safeQuery.isEmpty()) {
            displayedDishesList.addAll(allDishesList)
        } else {
            val lowerCaseQuery = safeQuery.lowercase()
            for (dish in allDishesList) {

                if (dish.title.lowercase().contains(lowerCaseQuery)

                ) {
                    displayedDishesList.add(dish)
                }
            }
        }

        if (::dishAdapter.isInitialized) {
            dishAdapter.notifyDataSetChanged()
        } else if (::rvDishes.isInitialized && rvDishes.adapter != null) {

            (rvDishes.adapter as? DishAdapter)?.notifyDataSetChanged()

        }
    }
}