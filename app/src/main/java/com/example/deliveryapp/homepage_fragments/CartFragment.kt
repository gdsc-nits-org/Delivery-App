package com.example.deliveryapp.homepage_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.models.CartItem
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragmentNavigation: HomepageNavigation? = null
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

        initializeViews(rootView)
        setupListeners()
        setupRecyclerView()
        showShimmerEffect()
        fetchCartItems()

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
        checkoutButton = rootView.findViewById(R.id.button)
    }

    private fun setupListeners() {
        val backButton = view?.findViewById<MaterialCardView>(R.id.button6)
        backButton?.setOnClickListener {
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
        cartAdapter = CartAdapter(emptyList())
        cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
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

    private fun fetchCartItems() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            db.collection("Users").document(currentUser.email ?: "")
                .collection("Cart")
                .get()
                .addOnSuccessListener { documents ->
                    val cartItems = documents.mapNotNull { document ->
                        val id = document.getString("id") ?: return@mapNotNull null
                        val quantity = document.getLong("quantity")?.toInt() ?: return@mapNotNull null
                        val price = document.getDouble("price") ?: return@mapNotNull null
                        CartItem(id, quantity, price)
                    }
                    cartAdapter.updateCartItems(cartItems)
                    updateTotalPrice()
                    hideShimmerEffect()
                }
                .addOnFailureListener { exception ->
                    println("Failed to fetch cart items: ${exception.message}")
                    hideShimmerEffect()
                }
        } else {
            hideShimmerEffect()
        }
    }

    private fun updateTotalPrice() {
        val totalPrice = cartAdapter.getTotalPrice()
        val deliveryCharge = 22.0 // Assuming a fixed delivery charge
        val finalTotal = totalPrice + deliveryCharge

        priceTextView.text = String.format("₹%.2f", totalPrice)
        deliveryChargeTextView.text = String.format("₹%.2f", deliveryCharge)
        totalPriceTextView.text = String.format("₹%.2f", finalTotal)
    }

    private fun applyPromoCode() {
        val promoCode = promoCodeEditText.text.toString()

    }

    private fun proceedToCheckout() {

    }
}