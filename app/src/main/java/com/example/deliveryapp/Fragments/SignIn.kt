package com.example.deliveryapp.Fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

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


        val verification = FirebaseAuth.getInstance().currentUser?.isEmailVerified

        if(verification == false)
            printToast("Please Verify Your Email")

        navController= Navigation.findNavController(view)
        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_loginPage)
        }
        binding.btnSignUp.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_signUpPage)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun printToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }


}