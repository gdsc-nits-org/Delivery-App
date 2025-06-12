package com.example.deliveryapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.deliveryapp.Dishes.ShopDishes
import com.example.deliveryapp.R
import com.example.deliveryapp.RoomDatabase.ADatabase
import com.example.deliveryapp.RoomDatabase.Orders
import com.example.deliveryapp.adapters.ShopDetailsAdapter
import com.example.deliveryapp.databinding.FragmentShopDetailsBinding
import com.example.deliveryapp.homepage_fragments.HomeFragment
import com.example.deliveryapp.utils.FirebaseManager
import com.example.deliveryapp.utils.FirestoreManager
import com.example.deliveryapp.utils.ShopData
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val DB_NAME = "Local_database.db"
class ShopDetailsFragment : Fragment() {

    private lateinit var firestore: FirestoreManager
    private var dishes: MutableList<ShopDishes> = arrayListOf()
    private lateinit var roomDB: ADatabase
    private lateinit var db: FirebaseDatabase
    private lateinit var binding: FragmentShopDetailsBinding
    private lateinit var shopID: String
    private lateinit var shopName: String
    private lateinit var shopData: ShopData
    private lateinit var adapter: ShopDetailsAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopDetailsBinding.inflate(inflater, container, false)
        shimmerFrameLayout = binding.shimmerFrameLayout // Assume this is in your layout
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        startShimmer()
        loadData()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_container, HomeFragment())
                fragmentTransaction.commitNow()
            }
        })
    }

    private fun init() {
        firestore = FirestoreManager()
        roomDB = ADatabase.getDatabase(requireContext())
        db = FirebaseManager.getFirebaseDatabase()
        shopName = arguments?.getString("ShopID") ?: ""
        adapter = ShopDetailsAdapter(dishes) { position ->
            var quantity = 0
            binding.layoutCounter.visibility = View.VISIBLE
            binding.textCounter.text = quantity.toString()

            binding.textCounter.isClickable = false
            binding.textCounter.isFocusable = false


            binding.plusBtn.setOnClickListener{
                quantity++
                binding.textCounter.text = quantity.toString()
            }
            binding.minusBtn.setOnClickListener{
                if(quantity > 1)
                    quantity--
                else quantity = 0
                binding.textCounter.text = quantity.toString()
            }
            if(quantity > 0) {
                binding.textCounter.isClickable = true
                binding.textCounter.isFocusable = true
            }
            else{
                binding.textCounter.isClickable = false
                binding.textCounter.isFocusable = false
            }
            binding.textCounter.setOnClickListener{
                val todoDao = roomDB.orderDao()
                if(dishes.size > 0 && quantity > 0){
                    val dish = dishes[position]
                    val order = Orders(
                        dish.title,
                        dish.imageUrl,
                        dish.resName,
                        dish.price,
                        quantity,
                        0
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            todoDao.insertOrder(order)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun startShimmer() {
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        binding.mainContent.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        binding.mainContent.visibility = View.VISIBLE
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                shopData = fetchShopData()
                dishes = fetchDishData()

                setUpViews()
                stopShimmer()
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
                stopShimmer()
            }
        }
    }

    private suspend fun fetchShopData(): ShopData = suspendCoroutine { continuation ->
        firestore.getDocumentById("Shops", shopName,
            onSuccess = { documentSnapshot ->
                val shopName = documentSnapshot?.getString("ShopName") ?: ""
                val imageUrl = documentSnapshot?.getString("ShopImg") ?: ""
                val location = documentSnapshot?.getString("Location") ?: ""
                val phoneNo = documentSnapshot?.getString("Phoneno") ?: ""
                val status = documentSnapshot?.getBoolean("Status") ?: false
                val delivery = documentSnapshot?.getBoolean("Delivery") ?: false
                val rating = documentSnapshot?.getString("Rating") ?: "0.0"
                shopID = documentSnapshot?.getString("ShopID")?: ""
                continuation.resume(ShopData(shopName, imageUrl, location, phoneNo, status, delivery, rating))
            },
            onFailure = { exception ->
                continuation.resumeWithException(exception)
            }
        )
    }

    private suspend fun fetchDishData(): MutableList<ShopDishes> = suspendCoroutine { continuation ->
        val query = db.getReference("dishes").orderByChild("shopId").equalTo(shopID)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dishList = mutableListOf<ShopDishes>()
                for (dish in snapshot.children) {
                    val title = dish.child("dishName").getValue(String::class.java) ?: ""
                    val imageUrl = dish.child("imageUrl").getValue(String::class.java) ?: ""
                    val price = dish.child("price").getValue(Int::class.java).toString()
                    val shopID = dish.child("shopId").getValue(String::class.java) ?: ""

                    dishList.add(ShopDishes(title, imageUrl, shopID, price))
                }
                continuation.resume(dishList)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpViews() {
        Glide.with(requireContext())
            .load(shopData.imageUrl)
            .into(binding.shopImage)

        binding.shopName.text = shopData.shopName
        adapter.updateDishes(dishes)
    }

}
