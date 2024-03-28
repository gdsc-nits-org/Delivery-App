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
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}