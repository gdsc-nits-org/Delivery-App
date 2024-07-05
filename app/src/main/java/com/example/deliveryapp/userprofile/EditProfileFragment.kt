package com.example.deliveryapp.userprofile

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.deliveryapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var databaseReference: DatabaseReference
    private var userData = UserData()
    private lateinit var etName : TextInputEditText
    private lateinit var etEmail : TextInputEditText
    private lateinit var etBio : TextInputEditText
    private lateinit var etContact : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        userData = UserData(
//            etName.text.toString(),
//            etEmail.text.toString(),
//            etContact.text.toString(),
//            etBio.text.toString()
//        )
////        Log.d("Ankit", "Saved Instance")
//        outState.putParcelable("userData", userData)
//    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etContact = view.findViewById(R.id.etContact)
        etBio = view.findViewById(R.id.etBio)

        if (savedInstanceState != null) {
            val userdata = savedInstanceState.getParcelable("userData", UserData::class.java)
            userdata?.let {
                userData = userdata
                etName.setText(userData.name)
                etEmail.setText(userData.email)
                etContact.setText(userData.contact)
                etBio.setText(userData.bio)
            }
            Log.d("Ankit", "Saved Instance")
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val backBtn = view.findViewById<ImageButton>(R.id.btnBack)
        backBtn.setOnClickListener{
            Toast.makeText(context, "Pressed back button", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }
        val tvEdit = view.findViewById<TextView>(R.id.tvEdit)

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        tvEdit.setOnClickListener {
            Toast.makeText(context, "Edit Your Profile", Toast.LENGTH_SHORT).show()
            setEditTextEnabled(true)
            btnSave.isEnabled = true
            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val contact = etContact.text.toString()
                val bio = etBio.text.toString()

                userData = UserData(name, email, contact, bio)

                databaseReference = FirebaseDatabase.getInstance().getReference("UserData")
                databaseReference.child(contact).setValue(userData).addOnSuccessListener {

                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    private fun setEditTextEnabled(state : Boolean)
    {
        etName.isEnabled = state
        etEmail.isEnabled = state
        etContact.isEnabled = state
        etBio.isEnabled = state
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}