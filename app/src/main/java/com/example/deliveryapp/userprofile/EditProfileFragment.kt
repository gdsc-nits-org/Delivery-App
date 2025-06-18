package com.example.deliveryapp.userprofile

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.deliveryapp.R
import com.example.deliveryapp.utils.FirebaseManager
import com.example.deliveryapp.utils.FirestoreManager
import com.example.deliveryapp.utils.UserData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileFragment : Fragment() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etContact: TextInputEditText
    private lateinit var etBio: TextInputEditText
    private lateinit var tvEdit: TextView
    private lateinit var btnSave: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etContact = view.findViewById(R.id.etContact)
        etBio = view.findViewById(R.id.etBio)
        tvEdit = view.findViewById(R.id.tvEdit)
        btnSave = view.findViewById(R.id.btnSave)

        // Firebase setup
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        // Load existing user data
        loadUserData()

        // Handle edit button
        tvEdit.setOnClickListener {
            setFieldsEditable(true)
            btnSave.isEnabled = true
        }

        // Handle save button
        btnSave.setOnClickListener {
            saveUserData()
        }

        return view
    }

    private fun setFieldsEditable(editable: Boolean) {
        etName.isEnabled = editable
        etEmail.isEnabled = editable
        etContact.isEnabled = editable
        etBio.isEnabled = editable
    }

    private fun loadUserData() {
        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                etName.setText(snapshot.child("name").value?.toString() ?: "")
                etEmail.setText(snapshot.child("email").value?.toString() ?: "")
                etContact.setText(snapshot.child("contact").value?.toString() ?: "")
                etBio.setText(snapshot.child("bio").value?.toString() ?: "")
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserData() {
        val updatedData = mapOf(
            "name" to etName.text.toString(),
            "email" to etEmail.text.toString(),
            "contact" to etContact.text.toString(),
            "bio" to etBio.text.toString()
        )

        databaseReference.updateChildren(updatedData).addOnSuccessListener {
            Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
            setFieldsEditable(false)
            btnSave.isEnabled = false
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show()
        }
    }
}

