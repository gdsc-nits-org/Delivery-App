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
import com.google.firebase.firestore.FirebaseFirestore

class EditAddressFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreManager: FirestoreManager
    private lateinit var firestoreDB: FirebaseFirestore
    private lateinit var userID: String
    private lateinit var binding: FragmentEditAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        auth = FirebaseManager.getFirebaseAuth()
        firestoreManager = FirestoreManager()
        firestoreDB = FirebaseManager.getFirebaseFirestore()
        userID = auth.currentUser?.email.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load existing address from Firestore
        loadExistingAddress()

        // Handle back button
        binding.btnBack.setOnClickListener {
            val fragment = parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, ProfileListFragment())
            fragment.commit()
            parentFragmentManager.popBackStack()
        }

        // Handle save button
        binding.btnSaveAddress.setOnClickListener {
            val hostel = binding.etHostelNameAddressPage.text.toString().trim()
            val street = binding.etStreetNameAddressPage.text.toString().trim()

            if (hostel.isEmpty() || street.isEmpty()) {
                Toast.makeText(requireContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                save(hostel, street)
            }
        }
    }

    private fun loadExistingAddress() {
        firestoreDB.collection("Users").document(userID).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val address = document.get("Address") as? Map<*, *>
                    address?.let {
                        binding.etHostelNameAddressPage.setText(it["HostelName"] as? String ?: "")
                        binding.etStreetNameAddressPage.setText(it["Street"] as? String ?: "")
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load address", Toast.LENGTH_SHORT).show()
            }
    }

    private fun save(hostel: String, street: String) {
        val data = mapOf(
            "Address" to mapOf(
                "HostelName" to hostel,
                "Street" to street
            )
        )

        firestoreDB.collection("Users").document(userID)
            .set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Address updated successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update address", Toast.LENGTH_SHORT).show()
            }
    }
}
