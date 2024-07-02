package com.example.deliveryapp.homepage_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.deliveryapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class CartFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragmentNavigation: HomepageNavigation? = null

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

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        val backButton = rootView.findViewById<MaterialCardView>(R.id.button6)
        backButton.setOnClickListener {
            // Ensure the fragment is not already being replaced
            if (fragmentNavigation != null) {
                fragmentNavigation?.replaceFragment(HomeFragment())
                bottomNavigationView.selectedItemId = R.id.bottom_home
            }
        }

        return rootView
    }
}
