package com.example.deliveryapp.homepage_fragments

import android.Manifest
import android.annotation.SuppressLint
import com.facebook.shimmer.ShimmerFrameLayout
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.adapters.CarouselImageAdapter
import com.example.deliveryapp.adapters.NestedRecyclerAdapter
import com.example.deliveryapp.models.CarouselImageItem
import com.example.deliveryapp.models.NestedRecyclerModelFood
import com.example.deliveryapp.models.NestedRecyclerModelMain
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.example.deliveryapp.utils.FirebaseManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class HomeFragment : Fragment() {
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var searchEditText: TextInputEditText
    private lateinit var rootView: View
    private var allShops: List<NestedRecyclerModelFood> = emptyList()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var firestore: FirebaseFirestore
    private var fragmentNavigation: HomepageNavigation? = null
    private lateinit var imageList: ArrayList<CarouselImageItem>
    private lateinit var nestedRecyclerAdapter: NestedRecyclerAdapter
    private lateinit var rvMain: RecyclerView

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomepageNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCarousel(imageList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        imageList = arrayListOf(
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://firebasestorage.googleapis.com/v0/b/delivery-app-9324d.appspot.com/o/images%2F1717689480794.jpg?alt=media&token=ee0e2637-67e5-4a7e-9c77-070a8419cd70"
            ),
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://firebasestorage.googleapis.com/v0/b/delivery-app-9324d.appspot.com/o/images%2F1717689480794.jpg?alt=media&token=ee0e2637-67e5-4a7e-9c77-070a8419cd70"
            ),
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://fastly.picsum.photos/id/320/500/500.jpg?hmac=2iE7TIF9kIqQOHrIUPOJx2wP1CJewQIZBeMLIRrm74s"
            ),
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://fastly.picsum.photos/id/798/500/500.jpg?hmac=Bmzk6g3m8sUiEVHfJWBscr2DUg8Vd2QhN7igHBXLLfo"
            ),
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://fastly.picsum.photos/id/95/500/500.jpg?hmac=0aldBQ7cQN5D_qyamlSP5j51o-Og4gRxSq4AYvnKk2U"
            ),
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://fastly.picsum.photos/id/778/500/500.jpg?hmac=jZLZ6WV_OGRxAIIYPk7vGRabcAGAILzxVxhqSH9uLas"
            )
        )
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerFrameLayout)
        rvMain = rootView.findViewById(R.id.rvMain)

        showShimmerEffect()

        setupCarousel(rootView)
        setupNestedRecyclerView(rootView)
        setupProfileCard(rootView)
        checkNotificationPermission()
        setupSearchView(rootView)

        return rootView
    }
    private fun showShimmerEffect() {
        shimmerFrameLayout.visibility = View.VISIBLE
        rvMain.visibility = View.GONE
        shimmerFrameLayout.startShimmer()
    }

    private fun hideShimmerEffect() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        rvMain.visibility = View.VISIBLE
    }
    private fun setupSearchView(rootView: View) {
        searchEditText = rootView.findViewById(R.id.searchbox)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterShops(s.toString())
            }
        })
    }

    private fun filterShops(query: String?) {
        if (query.isNullOrBlank()) {
            showAllCategories()
        } else {
            val filteredShops = allShops.filter { shop ->
                shop.shopName.contains(query, ignoreCase = true)
            }
            updateRecyclerView(listOf(NestedRecyclerModelMain("Search Results", filteredShops)))
        }
    }
    private fun showAllCategories() {
        val collections = listOf(
            NestedRecyclerModelMain("At Your DoorStep", allShops),
            NestedRecyclerModelMain("Shop By Shop", allShops.reversed()),
            NestedRecyclerModelMain("Treat Yourself", allShops.shuffled()),
            NestedRecyclerModelMain("Shops and Restaurant", allShops)
        )
        nestedRecyclerAdapter.updateData(collections)
    }
    private fun updateRecyclerView(collections: List<NestedRecyclerModelMain>) {
        nestedRecyclerAdapter.updateData(collections)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupCarousel(rootView: View) {
        val imageRV = rootView.findViewById<RecyclerView>(R.id.imageRV)
        val carouselImageAdapter = CarouselImageAdapter()
        imageRV.adapter = carouselImageAdapter
        carouselImageAdapter.submitList(imageList)
    }

    private fun setupNestedRecyclerView(rootView: View) {
        rvMain = rootView.findViewById(R.id.rvMain)
        nestedRecyclerAdapter = NestedRecyclerAdapter(emptyList())
        rvMain.adapter = nestedRecyclerAdapter
        fetchBanners()
        fetchShopData()
    }

    private fun setupProfileCard(rootView: View) {
        val cardView = rootView.findViewById<MaterialCardView>(R.id.home_profile)
        cardView.setOnClickListener {
            fragmentNavigation?.replaceFragment(ProfileListFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_profile
        }
    }

    private fun fetchShopData() {
        val db = FirebaseManager.getFirebaseFirestore()
        db.collection("Shops")
            .get()
            .addOnSuccessListener { result ->
                allShops = result.documents.mapNotNull { document ->
                    val imageUrl = document.getString("ShopImg") ?: return@mapNotNull null
                    val shopName = document.getString("ShopName") ?: "Unknown Shop"
                    val totalOrders = document.getLong("TotalOrders")?.toInt() ?: 0
                    val phoneNo = document.getString("Phoneno") ?: "N/A"
                    val location = document.getString("Location") ?: "N/A"

                    NestedRecyclerModelFood(imageUrl, shopName, totalOrders, phoneNo, location)
                }

                if (allShops.isNotEmpty()) {
                    showAllCategories()
                    hideShimmerEffect()
                } else {
                    Toast.makeText(context, "No shops found", Toast.LENGTH_SHORT).show()
                    hideShimmerEffect()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error fetching shop data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun fetchBanners() {
        firestore = FirebaseManager.getFirebaseFirestore()
        val items = arrayListOf<CarouselImageItem>()
        firestore.collection("Banners").get().addOnSuccessListener {banners->
            for(banner in banners){
                val name = UUID.randomUUID().toString()
                val url = banner.getString("url").toString()
//                Log.d("Banner", "Banner: $name \n $url")
                items.add(CarouselImageItem(name, url))
            }
            imageList.clear()
            imageList.addAll(items)
            refreshCarousel(imageList)
        }
            .addOnFailureListener{
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshCarousel(imageList: ArrayList<CarouselImageItem>) {
        val imageRV = rootView.findViewById<RecyclerView>(R.id.imageRV)
        val carouselImageAdapter = imageRV.adapter as? CarouselImageAdapter
        carouselImageAdapter?.submitList(imageList)
        carouselImageAdapter?.notifyDataSetChanged()
    }
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionDialog()
            }
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Notification Permission")
            .setMessage("This app needs notification permissions to send you updates.")
            .setPositiveButton("Allow") { _, _ ->
                requestNotificationPermission()
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATIONS
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentNavigation = null
    }
}