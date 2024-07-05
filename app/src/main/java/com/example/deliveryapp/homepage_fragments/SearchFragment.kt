package com.example.deliveryapp.homepage_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.deliveryapp.Dishes.DishItems
import com.example.deliveryapp.Dishes.ShopsFragment
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSearchBinding
import com.example.deliveryapp.utils.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
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
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        binding.backButton.setOnClickListener {
            fragmentNavigation?.replaceFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.bottom_home
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = listOf(ShopsFragment(), DishItems())
        val adapter = ViewPagerAdapter(fragments, childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.shops)
                1 -> getString(R.string.dishes)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}