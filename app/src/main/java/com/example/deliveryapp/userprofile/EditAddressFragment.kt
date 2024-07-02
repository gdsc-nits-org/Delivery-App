package com.example.deliveryapp.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.FragmentEditAddressBinding
import com.example.deliveryapp.utils.FirebaseManager
import com.example.deliveryapp.utils.FirestoreManager
import com.google.firebase.auth.FirebaseAuth

class EditAddressFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreManager: FirestoreManager
    private lateinit var userID : String
    private lateinit var binding: FragmentEditAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        auth = FirebaseManager.getFirebaseAuth()
        firestoreManager = FirestoreManager()
        userID = auth.currentUser!!.email.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentEditAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtnAddressPage.setOnClickListener{
            val fragment = parentFragmentManager.beginTransaction().replace(R.id.frame_container, ProfileListFragment())
            fragment.commit()
            parentFragmentManager.popBackStack()
        }

        binding.btnSaveAddress.setOnClickListener {
            val hostel = binding.etHostelNameAddressPage.text.toString().trim()
            if(hostel.isEmpty() or hostel.isBlank())
                Toast.makeText(requireContext(), "You are missing out on hostel!!", Toast.LENGTH_SHORT).show()
            else
            save(hostel)
        }
    }

    private fun save(hostel: String) {

        val data = mapOf("HostelName" to hostel)

        firestoreManager.updateDocument("Users", data, userID)
        Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }
}