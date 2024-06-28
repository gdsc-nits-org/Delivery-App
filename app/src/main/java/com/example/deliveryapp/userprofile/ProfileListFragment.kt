package com.example.deliveryapp.userprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.deliveryapp.MainActivity
import com.example.deliveryapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class ProfileListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvProfile = view.findViewById<TextView>(R.id.tvProfile1)
        tvProfile.setOnClickListener{
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.frame_container, EditProfileFragment())
//            val navBar = activity?.findViewById<BottomNavigationView>(R.id.bvNavBar)
//            if (navBar != null) {
//                navBar.visibility = View.INVISIBLE
//            }
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
//        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bvNavBar)
//        if (navBar != null) {
//            navBar.visibility = View.VISIBLE
//        }
        tvAddress.setOnClickListener{
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_container, AddressFragment())
//            if (navBar != null) {
//                navBar.visibility = View.INVISIBLE
//            }
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val tvLogOut = view.findViewById<TextView>(R.id.tvLogOut)
        tvLogOut.setOnClickListener{
            logout()
        }
    }
    private fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()
        val it = Intent(requireContext(), MainActivity::class.java)
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
        requireActivity().finish()
    }
}