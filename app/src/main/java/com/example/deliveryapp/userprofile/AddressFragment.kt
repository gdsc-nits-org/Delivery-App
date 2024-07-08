package com.example.deliveryapp.userprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.deliveryapp.Fragments.LocationFragment
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentAddressBinding
import com.example.deliveryapp.homepage_fragments.HomepageNavigation
import com.example.deliveryapp.utils.FirebaseManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var navController: NavController
    private lateinit var firestoreDB : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userID: String
    private var fragmentNavigation: HomepageNavigation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomepageNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        auth = FirebaseManager.getFirebaseAuth()
        firestoreDB = FirebaseManager.getFirebaseFirestore()
        userID = auth.currentUser?.email.toString()
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Ensure the fragment is not already being replaced
        binding.backBtnAddressPage.setOnClickListener{
            if (fragmentNavigation != null) {
                fragmentNavigation?.replaceFragment(LocationFragment())
            }
        }


        val etHostel = view.findViewById<TextInputEditText>(R.id.etHostelNameAddressPage)
        val etCity = view.findViewById<TextInputEditText>(R.id.etCityAddressPage)
        val etState = view.findViewById<TextInputEditText>(R.id.etStateNameAddressPage)
        val etCountry = view.findViewById<TextInputEditText>(R.id.etCountryNameAddressPage)
        val etPinCode = view.findViewById<TextInputEditText>(R.id.etPostalCodeAddressPage)

        val btnSave = view.findViewById<Button>(R.id.btnSaveAddress)
        btnSave.setOnClickListener {
            val address = AddressData(
                etHostel.text.toString().trim(), etCity.text.toString().trim(), etState.text.toString().trim(),
                etCountry.text.toString().trim(), etPinCode.text.toString().trim()
            )
            val verification = FirebaseAuth.getInstance().currentUser?.isEmailVerified
            if (verification == true)
            {
                if(isAdded) {
                    saveData(address)
                }
            }
            else {
                Toast.makeText(requireContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show()
                navController.navigateUp()
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
        private fun saveData(address: AddressData) {

            val data = mapOf(
                "HostelName" to address.hostel,
                "State" to address.state,
                "Postal Code" to address.pinCode,
                "City" to address.city,
                "Country" to address.country
            )
            firestoreDB.collection("Users").document(userID)
                .set(mapOf("Address" to data), SetOptions.merge()).addOnSuccessListener {
                Toast.makeText(requireContext(), "Address saved successfully", Toast.LENGTH_SHORT)
                    .show()
                    navController.navigate(R.id.action_addressFragment_to_homeActivity2)
                    requireActivity().finish()
            }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Error saving address: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
