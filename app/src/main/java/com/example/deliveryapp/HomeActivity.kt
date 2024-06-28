package com.example.deliveryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.deliveryapp.homepage_fragments.CartFragment
import com.example.deliveryapp.homepage_fragments.HomeFragment
import com.example.deliveryapp.homepage_fragments.HomepageNavigation
import com.example.deliveryapp.homepage_fragments.SearchFragment
import com.example.deliveryapp.userprofile.ProfileListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
class HomeActivity : AppCompatActivity(), HomepageNavigation {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    // Replace fragment with HomeFragment
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_cart -> {
                    // Replace fragment with CartFragment
                    replaceFragment(CartFragment())
                    true
                }
                R.id.bottom_search -> {
                    // Replace fragment with SearchFragment
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.bottom_profile -> {
                    // Replace fragment with ProfileFragment
                    replaceFragment(ProfileListFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}
