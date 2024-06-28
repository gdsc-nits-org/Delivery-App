package com.example.deliveryapp.homepage_fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.R
import com.example.deliveryapp.adapters.CarouselImageAdapter
import com.example.deliveryapp.adapters.NestedRecyclerAdapter
import com.example.deliveryapp.models.CarouselImageItem
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.example.deliveryapp.utils.SampleData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import java.util.UUID

class HomeFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        // Carousel
        val imageRV = rootView.findViewById<RecyclerView>(R.id.imageRV)

        // Data
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

        val nestedRecyclerAdapter = NestedRecyclerAdapter(SampleData.collections)
        val rvMain = rootView.findViewById<RecyclerView>(R.id.rvMain)
        rvMain.adapter = nestedRecyclerAdapter

        // set onClick to profile
        val cardView = rootView.findViewById<MaterialCardView>(R.id.home_profile)
        cardView.setOnClickListener {
            // Your onClick logic here
//            Toast.makeText(context, "Card clicked", Toast.LENGTH_SHORT).show()
            fragmentNavigation?.replaceFragment(ProfileListFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_profile
        }

        checkNotificationPermission()

        return rootView
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
