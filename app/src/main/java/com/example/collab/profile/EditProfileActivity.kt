package com.example.collab.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.SettingsActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var context: Context

    private lateinit var editTextName: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var professionEditText: EditText
    //private lateinit var instruments: TextView
    private lateinit var townshipEditText: EditText
    private lateinit var bioEditText: EditText

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        context = this

        val userId = intent.getStringExtra("UserId")
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        editTextName = findViewById(R.id.nameEditText)
        surnameEditText = findViewById(R.id.surnameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        professionEditText = findViewById(R.id.professionEditText)
        townshipEditText = findViewById(R.id.townshipEditText)
        bioEditText = findViewById(R.id.bioEditText)

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.menu.findItem(R.id.save).isVisible = true

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save -> {
                    savePersonProfileData()
                    true
                }
                else -> { false }
            }
        }

    }

    private fun savePersonProfileData() {
        val empName = editTextName.text.toString()
        val empAge = surnameEditText.text.toString()
        val empSalary = ageEditText.text.toString()
    }
}