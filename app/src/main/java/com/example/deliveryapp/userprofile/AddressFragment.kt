package com.example.deliveryapp.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.deliveryapp.R
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
            requireActivity().supportFragmentManager.popBackStack()
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }
}