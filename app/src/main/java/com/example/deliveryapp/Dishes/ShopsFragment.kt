package com.example.deliveryapp.Dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.adapters.ShopForYouAdapter

class ShopsFragment : Fragment() {

    private lateinit var adapter: ShopForYouAdapter
    private var dishes: MutableList<Dish> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shops, container, false)
        dishes.add(Dish("Biryani", R.drawable.barbeque, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb1, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb2, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb1, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb2, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb1, "NITS Cafe", "100"))
        dishes.add(Dish("Biryani", R.drawable.barb2, "NITS Cafe", "100"))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvForYou = view.findViewById<RecyclerView>(R.id.forYouRecyclerView)
        adapter = ShopForYouAdapter(dishes)
        rvForYou.adapter = adapter
    }
}