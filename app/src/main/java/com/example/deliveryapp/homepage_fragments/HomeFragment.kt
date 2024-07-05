package com.example.deliveryapp.homepage_fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class HomeFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragmentNavigation: HomepageNavigation? = null
    private lateinit var rvMain: RecyclerView
    private lateinit var nestedRecyclerAdapter: NestedRecyclerAdapter

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }

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
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        setupCarousel(rootView)
        setupNestedRecyclerView(rootView)
        setupProfileCard(rootView)
        checkNotificationPermission()

        return rootView
    }

    private fun setupCarousel(rootView: View) {
        val imageRV = rootView.findViewById<RecyclerView>(R.id.imageRV)
        val imageList = arrayListOf(
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://fastly.picsum.photos/id/866/500/500.jpg?hmac=FOptChXpmOmfR5SpiL2pp74Yadf1T_bRhBF1wJZa9hg"
            ),
            CarouselImageItem(
                UUID.randomUUID().toString(),
                "https://fastly.picsum.photos/id/270/500/500.jpg?hmac=MK7XNrBrZ73QsthvGaAkiNoTl65ZDlUhEO-6fnd-ZnY"
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
        val carouselImageAdapter = CarouselImageAdapter()
        imageRV.adapter = carouselImageAdapter
        carouselImageAdapter.submitList(imageList)
    }

    private fun setupNestedRecyclerView(rootView: View) {
        rvMain = rootView.findViewById(R.id.rvMain)
        nestedRecyclerAdapter = NestedRecyclerAdapter(emptyList())
        rvMain.adapter = nestedRecyclerAdapter

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
        val db = Firebase.firestore
        db.collection("Shops")
            .get()
            .addOnSuccessListener { result ->
                val shopList = result.documents.mapNotNull { document ->
                    val imageUrl = document.getString("ShopImg")
                    val status = document.getBoolean("Status")
                    val shopname = document.getString("ShopName")
                    if (imageUrl != null && status != null &&  shopname != null) {
                        NestedRecyclerModelFood(imageUrl, status,shopname)
                    } else {
                        null
                    }
                }

                if (shopList.isNotEmpty()) {
                    val collections = listOf(
                        NestedRecyclerModelMain("At Your DoorStep", shopList),
                        NestedRecyclerModelMain("Shop By Shop", shopList.reversed()),
                        NestedRecyclerModelMain("Treat Yourself", shopList.shuffled()),
                        NestedRecyclerModelMain("Shops and Restaurant", shopList)
                    )
                    nestedRecyclerAdapter.updateData(collections)
                } else {
                    Toast.makeText(context, "No shops found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error fetching shop data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
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