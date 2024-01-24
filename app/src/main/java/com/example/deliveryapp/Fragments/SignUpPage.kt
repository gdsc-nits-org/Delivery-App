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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException


class SignUpPage : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpPageBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }



    private fun init(view: View) {
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
    }
    private fun registerEvents() {
        binding.backBtnSignUpPage.setOnClickListener {
            navController.navigate(R.id.action_signUpPage_to_signIn)
        }
        binding.btnSignUp.setOnClickListener {
            val name=binding.etNameSignUpPage.text.toString().trim()
            val email=binding.etEmailSignUpPage.text.toString().trim()
            val pass=binding.etPassSignUpPage.text.toString().trim()
            val repass=binding.etRePassSignUpPage.text.toString().trim()

            val pattern=Regex("nits.ac.in")
            val check=pattern.containsMatchIn(email)
            
            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && repass.isNotEmpty() && check==true){
                if (pass==repass){
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context,"Registered Successfully",Toast.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_signUpPage_to_emptyActivity)

                            }else{
                                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
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