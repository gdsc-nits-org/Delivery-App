package com.example.deliveryapp.userprofile

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditProfileFragment : Fragment() {
    private lateinit var firestore: FirestoreManager
    private lateinit var userID : String
    private lateinit var auth : FirebaseAuth
    private lateinit var etName : TextInputEditText
    private lateinit var etEmail : TextInputEditText
    private lateinit var etBio : TextInputEditText
    private lateinit var etContact : TextInputEditText
    private lateinit var database : FirebaseDatabase
    private lateinit var btnSave : Button
    private lateinit var loadingOverlay: FrameLayout
    private lateinit var userData: UserData



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        loadingOverlay = view.findViewById(R.id.loadingOverlay)
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.coroutineContext.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews(view)

        val context = requireContext()

        val backBtn = view.findViewById<ImageButton>(R.id.btnBack)

        val tvEdit = view.findViewById<TextView>(R.id.tvEdit)

        backBtn.setOnClickListener{
            Toast.makeText(context, "Pressed back button", Toast.LENGTH_SHORT).show()
            val fragmentTransaction = parentFragmentManager.beginTransaction().replace(R.id.frame_container, ProfileListFragment())
            fragmentTransaction.commit()
            parentFragmentManager.popBackStack()
        }

        tvEdit.setOnClickListener {
            Toast.makeText(context, "Edit Your Profile", Toast.LENGTH_SHORT).show()
            enableViews(true)
            btnSave.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val contact = etContact.text.toString().trim()
                val bio = etBio.text.toString().trim()

                if(name != ""){
                    userData.name = name
                }
                if(contact != ""){
                    userData.contact = contact
                }
                if(email != ""){
                    userData.email = email
                }
                if(bio != ""){
                    userData.bio = bio
                }
                if(name.isEmpty() or name.isBlank()){
                    Toast.makeText(requireContext(),
                        "Oops! how can you be so nameless!!",
                        Toast.LENGTH_SHORT).show()
                }
                else
                {
                    saveData(userData)
                    enableViews(false)
                }
            }
        }
    }
    private fun init() {
        firestore = FirestoreManager()
        database = FirebaseManager.getFirebaseDatabase()
        auth = FirebaseManager.getFirebaseAuth()
        userData = UserData("", "", "", "")
        userID = auth.currentUser?.email.toString()
        userData.email = userID
    }
    private fun setUpViews(view: View) {
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etContact = view.findViewById(R.id.etContact)
        etBio = view.findViewById(R.id.etBio)
        btnSave = view.findViewById(R.id.btnSave)
    }
    private fun enableViews(state : Boolean)
    {
        etName.isEnabled = state
        etContact.isEnabled = state
        etBio.isEnabled = state
        btnSave.isEnabled = state
    }
    private fun updateData(userData: UserData){
        val data = mapOf(
            "Name" to userData.name,
            "Email" to userData.email,
            "PhoneNo" to userData.contact,
            "Bio" to userData.bio
        )

        firestore.updateDocument(
            "Users",
            data,
            userID)
    }

    private fun showLoading() {
        loadingOverlay.visibility = View.VISIBLE
        loadingOverlay.animate().alpha(1f).duration = 200
    }

    private fun hideLoading() {
        loadingOverlay.animate().alpha(0f).withEndAction {
            loadingOverlay.visibility = View.GONE
        }.duration = 200
    }

    private fun setText(data: UserData) {
        etName.setText(data.name)
        etContact.setText(data.contact)
        etEmail.setText(data.email)
        etBio.setText(data.bio)
        hideLoading()
    }

    private fun saveData(updatedUserData: UserData) {
        lifecycleScope.launch {
            try {
                // Show loading overlay
                withContext(Dispatchers.Main) {
                    showLoading()
                }

                // Perform the update operation
                withContext(Dispatchers.IO) {
                    updateData(updatedUserData)
                }
                // Hide loading overlay and show success message
                withContext(Dispatchers.Main) {
                    userData = updatedUserData
                    updateUI()
                    Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Hide loading overlay and show error message
                withContext(Dispatchers.Main) {
                    hideLoading()
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI() {
        showLoading()
        firestore.getDocumentById(
            collection = "Users",
            documentId = userID,
            onSuccess = { document ->
                if (document != null) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        val name = document.getString("Name") ?: ""
                        val contact = document.getString("PhoneNo")?: ""
                        val email = document.getString("Email") ?: ""
                        val bio = document.getString("Bio") ?: ""

                        if(name != ""){
                            userData.name = name.trim()
                        }
                        if(contact != ""){
                            userData.contact = contact.trim()
                        }
                        if(email != ""){
                            userData.email = email.trim()
                        }
                        if(bio != ""){
                            userData.bio = bio.trim()
                        }
                        setText(userData)
                    }
                } else {
                    Toast.makeText(requireContext(), "No user data found", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { exception ->
                Toast.makeText(requireContext(), "Error fetching user data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}