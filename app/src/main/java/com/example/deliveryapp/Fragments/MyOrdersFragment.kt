package com.example.deliveryapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.models.Order
import com.example.deliveryapp.models.OrderItem
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.example.deliveryapp.utils.FirebaseManager
import com.example.deliveryapp.utils.MyOrdersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyOrdersFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyOrdersAdapter
    private val orderList = mutableListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_orders, container, false)

        recyclerView = view.findViewById(R.id.rv_order_items)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyOrdersAdapter(orderList)
        recyclerView.adapter = adapter

        fetchOrders()

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, ProfileListFragment())
                .commit()
        }

        return view
    }

    private fun fetchOrders() {
        val db = FirebaseManager.getFirebaseFirestore()
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        currentUserEmail?.let { email ->
            db.collection("Users").document(email).get()
                .addOnSuccessListener { userDocument ->
                    val orderIds = userDocument.get("Orders") as? List<String> ?: listOf()
                    fetchOrderDetails(orderIds)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to fetch orders: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchOrderDetails(orderIds: List<String>) {
        val db = FirebaseFirestore.getInstance()

        orderIds.forEach { orderId ->
            db.collection("Orders").document(orderId).get()
                .addOnSuccessListener { orderDocument ->
                    val dateTime = orderDocument.getString("dateTime") ?: ""
                    val deliveryStatus = orderDocument.getString("deliveryStatus") ?: ""
                    val itemsData = orderDocument.get("items") as? List<Map<String, Any>> ?: listOf()
                    val location = orderDocument.getString("location") ?: ""
                    val storeName = orderDocument.getString("storeName") ?: ""

                    val orderItems = itemsData.map { item ->
                        OrderItem(
                            item["Product Price"] as? Double ?: 0.0,
                            item["Product Title"] as? String ?: ""
                        )
                    }

                    val order = Order(
                        dateTime,
                        deliveryStatus,
                        orderItems,
                        location,
                        storeName
                    )

                    orderList.add(order)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to fetch order details: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}