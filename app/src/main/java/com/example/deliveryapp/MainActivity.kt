package com.example.deliveryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.deliveryapp.databinding.ActivityMainBinding
import com.example.deliveryapp.homepage_fragments.CartFragment
import com.example.deliveryapp.homepage_fragments.HomeFragment
import com.example.deliveryapp.homepage_fragments.ProfileFragment
import com.example.deliveryapp.homepage_fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    // Replace fragment with HomeFragment
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_cart -> {
                    // Replace fragment with HomeFragment
                    replaceFragment(CartFragment())
                    true
                }
                R.id.bottom_search -> {
                    // Replace fragment with HomeFragment
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.bottom_profile -> {
                    // Replace fragment with HomeFragment
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())

    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}