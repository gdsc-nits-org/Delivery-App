package com.example.deliveryapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class LoginPage : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginPageBinding
    private lateinit var navController: NavController
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Log.d("TAG", "Pressed...")
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun init(view: View) {

        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        databaseRef=FirebaseDatabase.getInstance()
            .reference.child("Users")
            .child(auth.currentUser?.uid.toString())

    }

    private fun registerEvents() {
        binding.tvLoginPage.setOnClickListener {
            navController.navigate(R.id.action_loginPage_to_signUpPage)
        }
        binding.backBtnLoginPage.setOnClickListener {
            navController.navigate(R.id.action_loginPage_to_signIn)
        }
        binding.btnLogin.setOnClickListener {
            val email=binding.etEmailLoginPage.text.toString().trim()
            val pass=binding.etPassLoginPage.text.toString().trim()


            val pattern=Regex("nits.ac.in")
            val check=pattern.containsMatchIn(email)

            if (email.isNotEmpty() && pass.isNotEmpty() && check){

                auth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val verification = auth.currentUser?.isEmailVerified
                            when(verification){
                                true -> {
                                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                                    navController.navigate(R.id.action_loginPage_to_locationFragment)
                                }
                                else -> Toast.makeText(requireContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Kindly Sign Up By Clicking SignUp button",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }else{

                Toast.makeText(context,"Please fill up all the necessary details",Toast.LENGTH_SHORT).show()

            }
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Log.d("TAG", "Pressed...")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}