package com.example.deliveryapp.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.deliveryapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            findNavController().navigate(R.id.action_profileListFragment_to_editProfileFragment)
            val navBar = activity?.findViewById<BottomNavigationView>(R.id.bvNavBar)
            if (navBar != null) {
                navBar.visibility = View.INVISIBLE
            }
        }
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bvNavBar)
        if (navBar != null) {
            navBar.visibility = View.VISIBLE
        }
        tvAddress.setOnClickListener{
            findNavController().navigate(R.id.action_profileListFragment_to_addressFragment)
            if (navBar != null) {
                navBar.visibility = View.INVISIBLE
            }
        }
    }
}