package com.example.collab.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.models.Person
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileActivity : AppCompatActivity() {

    private lateinit var context: Context
    var currentUser : Person? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var dateOfBirth: TextView
    private lateinit var profession: TextView
    private lateinit var instruments: TextView
    private lateinit var genres: TextView
    private lateinit var township: TextView
    private lateinit var bio: TextView
    private lateinit var profilePicture: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        context = this

        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        name = findViewById(R.id.name)
        surname = findViewById(R.id.surnameTextView)
        dateOfBirth = findViewById(R.id.dateOfBirthTextView)
        bio = findViewById(R.id.bioTextView)
        profession = findViewById(R.id.professionTextView)
        instruments = findViewById(R.id.instrumentsTextView)
        genres = findViewById(R.id.genresTextView)
        township = findViewById(R.id.townshipTextView)
        profilePicture = findViewById(R.id.personPhotoCircle)

        val cardUserId = intent.getStringExtra("cardUserId")

        getCardUserInfo(cardUserId!!)
    }

    private fun getCardUserInfo(cardUserId: String) {
        databaseReference.child(cardUserId).get().addOnSuccessListener {
            currentUser = it.getValue(Person::class.java)
            name.text = currentUser!!.name
            surname.text = currentUser!!.surname
            dateOfBirth.text = currentUser!!.dateOfBirth
            bio.text = currentUser!!.bio
            profession.text = currentUser!!.profession
            township.text = currentUser!!.township
            downloadProfileImage(cardUserId)
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun downloadProfileImage(cardUserId: String){
        storageReference = FirebaseStorage.getInstance().getReference("Users/$cardUserId")
        storageReference.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .into(profilePicture)
            Log.e("firebase", "Pic downloaded successfully $it")
        }.addOnFailureListener {
            Log.e("firebase", "Pic download failed", it)
        }
    }
}