package com.example.deliveryapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSignUpPageBinding
import com.example.deliveryapp.utils.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpPage : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpPageBinding
    private lateinit var navController: NavController
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }

    private fun init(view: View) {

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance()
            .reference.child("Users")

    }

    private fun registerEvents() {
        binding.backBtnSignUpPage.setOnClickListener {
            navController.navigate(R.id.action_signUpPage_to_signIn)
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.etNameSignUpPage.text.toString().trim()
            val email = binding.etEmailSignUpPage.text.toString().trim()
            val pass = binding.etPassSignUpPage.text.toString().trim()
            val repass = binding.etRePassSignUpPage.text.toString().trim()

            val pattern = Regex("nits.ac.in")
            val check = pattern.containsMatchIn(email)

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && repass.isNotEmpty() && check) {
                if (pass == repass) {

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val currentUser = auth.currentUser
                            val userId = currentUser?.uid

                            val userModel = User(name, email)
                            userId?.let {
                                databaseRef.child(it).setValue(userModel)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                            navController.navigate(R.id.action_signUpPage_to_locationFragment)
                                        } else {
                                            Toast.makeText(context, "User could not be added", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(context, authTask.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill up all the necessary details", Toast.LENGTH_SHORT).show()
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
