package com.example.deliveryapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.deliveryapp.R
import com.example.deliveryapp.userprofile.ProfileListFragment

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fvFragment, ProfileListFragment())
        fragmentTransaction.commit()
    }
}