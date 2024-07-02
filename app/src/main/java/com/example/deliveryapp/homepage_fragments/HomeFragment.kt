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
import com.example.deliveryapp.R
import com.example.deliveryapp.models.CarouselImageItem
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.example.deliveryapp.utils.FirebaseManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageList : ArrayList<CarouselImageItem>
    private lateinit var firestore: FirebaseFirestore
    private var fragmentNavigation: HomepageNavigation? = null

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomepageNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        firestore = FirebaseManager.getFirebaseFirestore()
        imageList = arrayListOf()
        fetchBanners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        // Carousel


        // set onClick to profile
        val cardView = rootView.findViewById<MaterialCardView>(R.id.home_profile)
        cardView.setOnClickListener {
            // Your onClick logic here
            Toast.makeText(context, "Card clicked", Toast.LENGTH_SHORT).show()
            fragmentNavigation?.replaceFragment(ProfileListFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_profile
        }

        checkNotificationPermission()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
    private fun fetchBanners() {

        val items = arrayListOf<CarouselImageItem>()
        firestore.collection("Banners").get().addOnSuccessListener {Banners->

            for(banner in Banners){
                val name = banner.get("Name").toString().trim()
                val url = banner.get("url").toString().trim()

                items.add(CarouselImageItem(name, url))
            }
            imageList = items
        }
            .addOnFailureListener{
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
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
