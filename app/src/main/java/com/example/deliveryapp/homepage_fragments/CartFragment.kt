package com.example.deliveryapp.homepage_fragments

import CartAdapter
import OrderViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.deliveryapp.Fragments.DB_NAME
import com.example.deliveryapp.R
import com.example.deliveryapp.RoomDatabase.ADatabase
import com.example.deliveryapp.models.CartItem
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class CartFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragmentNavigation: HomepageNavigation? = null
    private lateinit var backButton : MaterialCardView
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalPriceTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var deliveryChargeTextView: TextView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var promoCodeEditText: EditText
    private lateinit var applyPromoButton: LinearLayout
    private lateinit var addMoreButton: Button
    private lateinit var checkoutButton: LinearLayout
    private var cartItems : MutableList<CartItem> = arrayListOf()
    private lateinit var orderViewModel: OrderViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomepageNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_cart, container, false)
        return rootView
    }

    private fun initializeViews(rootView: View) {
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        cartRecyclerView = rootView.findViewById(R.id.cartRecyclerView)
        totalPriceTextView = rootView.findViewById(R.id.textView11)
        priceTextView = rootView.findViewById(R.id.textView4)
        deliveryChargeTextView = rootView.findViewById(R.id.textView8)
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrameLayout)
        promoCodeEditText = rootView.findViewById(R.id.editTextText2)
        applyPromoButton = rootView.findViewById(R.id.button3)
        addMoreButton = rootView.findViewById(R.id.button5)
        checkoutButton = rootView.findViewById(R.id.btncheckout)
        backButton = rootView.findViewById(R.id.button6)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupListeners()
        setupRecyclerView()
        updateTotalPrice()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            fragmentNavigation?.replaceFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_home
        }

        applyPromoButton.setOnClickListener {
            applyPromoCode()
        }

        addMoreButton.setOnClickListener {
            // Navigate to product listing or category page
        }

        checkoutButton.setOnClickListener {
            proceedToCheckout()
        }
    }

    private fun setupRecyclerView() {
        val orderDao = Room.databaseBuilder(
            requireContext(),
            ADatabase::class.java,
            DB_NAME
        ).build().orderDao()
        cartAdapter = CartAdapter(arrayListOf(), orderDao)
        cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        orderViewModel.allOrders.observe(viewLifecycleOwner){orders->
            for(order in orders){
                cartItems.add(CartItem(order.id, order.title, order.imageUrl, order.quantity, order.price.toInt()))
            }
            cartAdapter.updateCartItems(cartItems)
            updateTotalPrice()
        }
    }

    private fun showShimmerEffect() {
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        cartRecyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        cartRecyclerView.visibility = View.VISIBLE
    }

//    private fun fetchCartItems() {
//        val db = FirebaseFirestore.getInstance()
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (currentUser != null) {
//            db.collection("Users").document(currentUser.email ?: "")
//                .collection("Cart")
//                .get()
//                .addOnSuccessListener { documents ->
//                    val cartItems = documents.mapNotNull { document ->
//                        val id = document.getString("id") ?: return@mapNotNull null
//                        val quantity = document.getLong("quantity")?.toInt() ?: return@mapNotNull null
//                        val price = document.getLong("price") ?: return@mapNotNull null
//                        CartItem(id, quantity.toString(), price.toInt())
//                    }
//                    cartAdapter.updateCartItems(cartItems)
//                    updateTotalPrice()
//                    hideShimmerEffect()
//                }
//                .addOnFailureListener { exception ->
//                    println("Failed to fetch cart items: ${exception.message}")
//                    hideShimmerEffect()
//                }
//        } else {
//            hideShimmerEffect()
//        }
//    }

    private fun updateTotalPrice() {
        val totalPrice = cartAdapter.getTotalPrice()
        val deliveryCharge = 22 // Assuming a fixed delivery charge
        val finalTotal = totalPrice + deliveryCharge

        priceTextView.text = "₹${totalPrice}"
        deliveryChargeTextView.text = "₹${deliveryCharge}"
        totalPriceTextView.text = "₹${finalTotal}"
    }

    private fun applyPromoCode() {
        val promoCode = promoCodeEditText.text.toString()
    }

    private fun proceedToCheckout() {

    }
}