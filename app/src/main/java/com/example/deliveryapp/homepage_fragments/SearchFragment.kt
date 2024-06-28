package com.example.deliveryapp.homepage_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.deliveryapp.R
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class SearchFragment : Fragment() {
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
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)


        val rootView =  inflater.inflate(R.layout.fragment_search, container, false)

        // set on click to back button
        val backButton = rootView.findViewById<MaterialCardView>(R.id.imageView)
        backButton.setOnClickListener {
            fragmentNavigation?.replaceFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_home
        }

        return rootView
    }
}