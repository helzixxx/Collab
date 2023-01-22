package com.example.collab.cards

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collab.models.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CardsViewModel : ViewModel() {

    var users = MutableLiveData<ArrayList<Card?>>()

    private lateinit var databaseReference: DatabaseReference



//    suspend fun getCardUsersFromStorage() = withContext(Dispatchers.IO){
//        val auth = FirebaseAuth.getInstance()
//        val currentUserId = auth.currentUser!!.uid
//        var card : Card?
//        databaseReference = FirebaseDatabase.getInstance().reference
//        return@withContext try {
//            databaseReference.child("Users").addChildEventListener(object : ChildEventListener {
//                val usersCards = ArrayList<Card?>()
//                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                    if (dataSnapshot.exists() && dataSnapshot.key != currentUserId) {
//                        card = Card(
//                            dataSnapshot.key!!,
//                            dataSnapshot.child("name").value.toString(),
//                            dataSnapshot.child("profession").value.toString(),
//                            null
//                        )
//                        usersCards.add(card)
//                        Log.e("firebase", "Successfully got data $usersCards")
//                    }
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }
//    }
//
//    private fun downloadProfilePicture(
//        userId: String
//    ) : Uri? {
//        val storageReference = FirebaseStorage.getInstance()
//        var uri : Uri? = null
//        storageReference.getReference("Users/$userId").downloadUrl.addOnSuccessListener{
//            uri = it
//            Log.e("firebase pic success", " Uri: $it")
//        }.addOnFailureListener {
//            Log.e("firebase pic fail", " Uri: $it")
//        }
//
//        return uri
//    }
}