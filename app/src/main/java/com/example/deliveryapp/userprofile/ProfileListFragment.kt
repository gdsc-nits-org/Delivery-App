package com.example.deliveryapp.userprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.deliveryapp.Fragments.MyOrdersFragment
import com.example.deliveryapp.R
import com.example.deliveryapp.activities.MainActivity
import com.example.deliveryapp.utils.FirebaseManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileListFragment : Fragment() {

    private val TAG = "ProfileListFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val helloTextView = view.findViewById<TextView>(R.id.HelloUser)
        val firebaseUser: FirebaseUser? = FirebaseManager.getFirebaseAuth().currentUser

        if (firebaseUser == null) {
            helloTextView.text = "Hello User"
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = firebaseUser.uid
        Log.d(TAG, "Logged in user UID: $uid")

        val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "Firebase snapshot: ${snapshot.value}")

                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    Log.d(TAG, "Fetched name: $name")

                    if (!name.isNullOrBlank()) {
                        helloTextView.text = "Hello $name"
                    } else {
                        val fallback = firebaseUser.email?.substringBefore("@") ?: "User"
                        helloTextView.text = "Hello $fallback"
                        Toast.makeText(requireContext(), "Name not found, using fallback", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val fallback = firebaseUser.email?.substringBefore("@") ?: "User"
                    helloTextView.text = "Hello $fallback"
                    Toast.makeText(requireContext(), "User record not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val fallback = firebaseUser.email?.substringBefore("@") ?: "User"
                helloTextView.text = "Hello $fallback"
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Firebase error", error.toException())
            }
        })

        view.findViewById<TextView>(R.id.tvProfile1).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<TextView>(R.id.tvOrders).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, MyOrdersFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<TextView>(R.id.tvAddress).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, EditAddressFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<TextView>(R.id.tvLogOut).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        FirebaseManager.getFirebaseAuth().signOut()
        Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
