package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.collab.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingsActivity : AppCompatActivity() {
    lateinit var currentUserId: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        currentUserId = auth.currentUser!!.uid


        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Check", "1")
            startActivity(intent)
        }

        val appInfo = findViewById<Button>(R.id.appInfo)
        appInfo.setOnClickListener {
//            val intent = Intent(this, AppInfo::class.java)
//            startActivity(intent)
        }

        val devInfo = findViewById<Button>(R.id.devInfo)
        devInfo.setOnClickListener {

        }

        val regulamin = findViewById<Button>(R.id.regulamin)
        regulamin.setOnClickListener {

        }

        val deleteAccount = findViewById<Button>(R.id.deleteAccount)
        deleteAccount.setOnClickListener {
            showDeleteDialog()

        }


        val logOutBtn = findViewById<Button>(R.id.logOut)
        logOutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this)
        val alertDialog: AlertDialog = builder.create()
        builder.setTitle("Usunięcie profilu")
        builder.setMessage("Czy napewno chcesz usunąć swój proful?")


        builder.setPositiveButton("Tak") { dialog, which ->
            deleteUser()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        builder.setNegativeButton("Nie") { dialog, which ->
            alertDialog.cancel()
        }

        alertDialog.show()
    }

    private fun deleteUser(){
        val user = Firebase.auth.currentUser!!
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("firebase auth", "User account deleted.")
                } else {
                    Log.e("firebase auth", "User account not deleted.")
                }
            }.addOnFailureListener {
                Log.e("firebase auth", "$it")
            }
        databaseReference.child("Users").child(currentUserId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("firebase realtime", "User account deleted.")
                } else {
                    Log.e("firebase realtime", "User account not deleted.")
                }
            }.addOnFailureListener {
                Log.e("firebase realtime", "$it")
            }
        storageReference.child("Users").child(currentUserId).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("firebase storage", "User account deleted.")
                } else {
                    Log.e("firebase storage", "User account not deleted.")
                }
            }.addOnFailureListener {
                Log.e("firebase storage", "$it")
            }
    }
}