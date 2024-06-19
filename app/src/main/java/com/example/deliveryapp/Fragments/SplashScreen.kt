package com.example.deliveryapp.Fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth


class SplashScreen : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sideAnimation= AnimationUtils.loadAnimation(parentFragment?.context,R.anim.slide)
        binding.ivIcon.startAnimation(sideAnimation)
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        val verification = auth.currentUser?.isEmailVerified

        Handler(Looper.getMainLooper()).postDelayed({
            execute(verification)
        },3000)

//            Toast.makeText(requireContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing or handle as needed
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun execute(verification: Boolean?) {
        if (auth.currentUser !=null && verification == true){
            navController.navigate(R.id.action_splashScreen_to_homeActivity)
            this.requireActivity().finish()
        }else if(auth.currentUser != null && verification == false){
                navController.navigate(R.id.action_splashScreen_to_signIn)
        }
        else {
            navController.navigate(R.id.action_splashScreen_to_accessLocation)
        }
    }
}