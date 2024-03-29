package com.example.deliveryapp.Fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSignInBinding

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)
        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_loginPage)
        }
        binding.btnSignUp.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_signUpPage)
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Log.d("TAG", "Pressed...")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }


}