package com.example.deliveryapp.homepage_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.deliveryapp.Dishes.DishItems
import com.example.deliveryapp.R
import com.example.deliveryapp.Dishes.ShopsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout

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

        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        // set on click to back button
        val backButton = rootView.findViewById<MaterialCardView>(R.id.imageView)
        backButton.setOnClickListener {
            fragmentNavigation?.replaceFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_home
        }

        // Set up TabLayout listener
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ShopsFragment())
                        .commit()
                    1 -> childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, DishItems())
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Set default tab
        if (savedInstanceState == null) {
            tabLayout.getTabAt(0)?.select()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ShopsFragment())
                .commit()
        }

        return rootView
    }
}
