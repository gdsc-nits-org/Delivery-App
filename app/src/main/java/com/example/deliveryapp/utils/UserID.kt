package com.example.deliveryapp.utils

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UserID {

    private lateinit var db : FirebaseDatabase
    private lateinit var userID : String
    private
    fun fetchUserID(currentUser:  FirebaseUser) : String{

        db = FirebaseManager.getFirebaseDatabase()
        userID = ""
        currentUser.uid.let{
            db.reference
                .child("Users")
                .child(it)
                .child("email")
                .get().addOnSuccessListener {dataSnapshot->
                    userID = dataSnapshot.getValue(String::class.java).toString()
                }
                .addOnFailureListener{
                    println("Failed")
                }
        }
        return userID
    }
}