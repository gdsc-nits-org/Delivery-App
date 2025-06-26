package com.example.deliveryapp.Dishes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.adapters.ShopForYouAdapter

class ShopsFragment : Fragment() {

    private lateinit var adapter: ShopForYouAdapter
    private var allItemsMasterList: MutableList<Dish> = mutableListOf()
    private var displayedItemsList: MutableList<Dish> = mutableListOf()
    private lateinit var rvForYou: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shops, container, false)

        if (allItemsMasterList.isEmpty()) {
            prepareMasterItemsList()
        }
        displayedItemsList.clear()
        displayedItemsList.addAll(allItemsMasterList)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvForYou = view.findViewById(R.id.forYouRecyclerView)
//        rvForYou.layoutManager = LinearLayoutManager(context)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvForYou.layoutManager = layoutManager


        adapter = ShopForYouAdapter(displayedItemsList)
        rvForYou.adapter = adapter

        val initialQuery = arguments?.getString("initialQuery") ?: ""
        filterShops(initialQuery)
    }


    private fun prepareMasterItemsList() {
        allItemsMasterList.clear()

        allItemsMasterList.add(Dish("Biryani", R.drawable.barbeque, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb, "GH1 Canteen", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb1, "BH4 Canteen", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb2, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb1, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb2, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb1, "NITS Cafe", "100"))
        allItemsMasterList.add(Dish("Biryani", R.drawable.barb2, "NITS Cafe", "100"))
//        return view
    }

    fun filterShops(query: String) {
        Log.d("ShopsFragment", "Filtering shops with query: '$query'")
        displayedItemsList.clear()
        val safeQuery = query ?: ""

        if (safeQuery.isEmpty()) {
            displayedItemsList.addAll(allItemsMasterList)
        } else {
            val lowerCaseQuery = safeQuery.lowercase()
            for (item in allItemsMasterList) {

                if (
                    item.resName.lowercase().contains(lowerCaseQuery)
                ) {
                    displayedItemsList.add(item)
                }
            }
        }
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        } else if (::rvForYou.isInitialized && rvForYou.adapter != null) {

            (rvForYou.adapter as? ShopForYouAdapter)?.notifyDataSetChanged()
        }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val rvForYou = view.findViewById<RecyclerView>(R.id.forYouRecyclerView)
//        adapter = ShopForYouAdapter(dishes)
//        rvForYou.adapter = adapter
//    }
    }
}