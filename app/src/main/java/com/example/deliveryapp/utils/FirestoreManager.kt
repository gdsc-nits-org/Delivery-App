package com.example.deliveryapp.utils

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

class FirestoreManager {
    private val db = FirebaseManager.getFirebaseFirestore()

    fun addDocument(collection: String, userData: Map <String, Any>, docID: String) {
        db.collection(collection).document(docID).set(userData).addOnSuccessListener {
            Log.d("Upload", "Successful")
        }.addOnFailureListener{
            Log.d("Upload", "Unsuccessful")
        }
    }

    fun updateDocument(collection: String, userData: Map<String, Any>, documentId: String){
        db.collection(collection).document(documentId).update(userData).addOnSuccessListener {
            Log.d("Update", "Successful")
        }.addOnFailureListener{
            Log.d("Update", "Unsuccessful")
        }
    }
    fun getDocumentById(
        collection: String,
        documentId: String,
        onSuccess: (DocumentSnapshot?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collection).document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    onSuccess(document)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}