package com.example.deliveryapp.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentAddressBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)

        binding.backBtnAddressPage.setOnClickListener {
            navController.navigate(R.id.action_addressFragment_to_locationFragment)
            navController.popBackStack()
        }

        var etHostel = view.findViewById<TextInputEditText>(R.id.etHostelNameAddressPage)
        var etCity = view.findViewById<TextInputEditText>(R.id.etCityAddressPage)
        var etState = view.findViewById<TextInputEditText>(R.id.etStateNameAddressPage)
        var etCountry = view.findViewById<TextInputEditText>(R.id.etCountryNameAddressPage)
        var etPinCode = view.findViewById<TextInputEditText>(R.id.etPostalCodeAddressPage)

        var address = AddressData(
            etHostel.text.toString(), etCity.text.toString(), etState.text.toString(),
            etCountry.text.toString(), etPinCode.text.toString()
        )

        val btnSave=view.findViewById<Button>(R.id.btnSaveAddress)
        btnSave.setOnClickListener {
            etHostel.setText(address.hostel)
            etCountry.setText(address.city)
            etState.setText(address.state)
            etCountry.setText(address.country)
            etPinCode.setText(address.pinCode)
            val verification = FirebaseAuth.getInstance().currentUser?.isEmailVerified
            if (verification == true)
            {
                navController.navigate(R.id.action_addressFragment_to_homeActivity2)
                requireActivity().finish()
            }
            else {
                Toast.makeText(requireContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_addressFragment_to_loginPage)
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