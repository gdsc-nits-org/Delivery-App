package com.example.deliveryapp.Fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.example.deliveryapp.utils.MyOrdersAdapter
import com.example.deliveryapp.utils.Order

class MyOrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_my_orders, container, false)
        val orderList = listOf(
            Order("Johnny Rolls", "Bikini Bottom", "$10", "Coffee Pro Max", "10 Feb 2024 at 10:00 AM", "Delivered ✔"),
            // Add more orders here
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.orders_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MyOrdersAdapter(orderList)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction().replace(R.id.frame_container, ProfileListFragment())
            fragmentTransaction.commit()
            parentFragmentManager.popBackStack()
        }
        return view
    }
}