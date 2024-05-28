package com.example.deliveryapp.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.deliveryapp.R
import com.google.android.material.textfield.TextInputEditText


class AddressFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageButton>(R.id.imageButton)
        backButton.setOnClickListener {
           findNavController().popBackStack()
        }
        var etHostel = view.findViewById<TextInputEditText>(R.id.etHostel)
        var etCity = view.findViewById<TextInputEditText>(R.id.etCity)
        var etState = view.findViewById<TextInputEditText>(R.id.etState)
        var etCountry = view.findViewById<TextInputEditText>(R.id.etCountry)
        var etPinCode = view.findViewById<TextInputEditText>(R.id.etPinCode)

        var address = AddressData(etHostel.text.toString(), etCity.text.toString(), etState.text.toString(),
            etCountry.text.toString(), etPinCode.text.toString())

        val btnSave = view.findViewById<Button>(R.id.btnAddressSave)
        btnSave.setOnClickListener {
            etHostel.setText(address.hostel)
            etCountry.setText(address.city)
            etState.setText(address.state)
            etCountry.setText(address.country)
            etPinCode.setText(address.pinCode)
        }
    }
}