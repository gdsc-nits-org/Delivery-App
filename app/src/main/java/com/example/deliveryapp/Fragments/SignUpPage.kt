package com.example.deliveryapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentSignUpPageBinding
import com.example.deliveryapp.utils.FirebaseManager
import com.example.deliveryapp.utils.FirestoreManager
import com.example.deliveryapp.utils.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignUpPage : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpPageBinding
    private lateinit var navController: NavController
    private lateinit var databaseRef: DatabaseReference
    private lateinit var firestore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        registerEvents()
    }

    private fun init() {
        auth = FirebaseManager.getFirebaseAuth()

        firestore = FirebaseManager.getFirebaseFirestore()

        databaseRef = FirebaseManager.getFirebaseDatabase()
            .reference.child("Users")

    }

    @SuppressLint("SuspiciousIndentation")
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
                            uploadFirestore(userModel, userModel.email)
                            userId?.let {
                                databaseRef.child(it).setValue(userModel)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            currentUser.sendEmailVerification().addOnSuccessListener {
                                                Toast.makeText(requireContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show()
                                                Handler(Looper.getMainLooper()).postDelayed({
                                                    navController.navigate(R.id.action_signUpPage_to_loginPage)
                                                }, 1000)
                                            }
                                                .addOnFailureListener {error->
                                                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            Toast.makeText(context, "User could not be added", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            val verification = auth.currentUser?.isEmailVerified
                            if(verification == false)
                                Toast.makeText(requireContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show()
                            else
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
                navController.navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun uploadFirestore(userModel: User, userId: String) {
        val firestoreManager = FirestoreManager()

        val newData = mapOf(
            "Name" to userModel.name,
            "Email" to userModel.email,
        )
        firestoreManager.addDocument("Users", newData, userId)
        Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_SHORT).show()
    }
}
