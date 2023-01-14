package com.example.collab.profile

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.models.Person
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var context: Context

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var dateOfBirthLayout: LinearLayout
    private lateinit var dateOfBirthTextView: TextView
    private lateinit var professionEditText: EditText
    private lateinit var instrumentsLayout: LinearLayout
    private lateinit var genresLayout: LinearLayout
    private lateinit var townshipEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var profilePicture: ShapeableImageView

    var date: String = ""
    lateinit var currentUserId : String

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        context = this

        val auth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        currentUserId = auth.currentUser!!.uid

        nameEditText = findViewById(R.id.nameEditText)
        surnameEditText = findViewById(R.id.surnameEditText)
        dateOfBirthLayout = findViewById(R.id.spinnerContainerDateOfBirth)
        professionEditText = findViewById(R.id.professionEditText)
        instrumentsLayout = findViewById(R.id.spinnerContainerInstrument)
        genresLayout = findViewById(R.id.spinnerContainerGenre)
        townshipEditText = findViewById(R.id.townshipEditText)
        bioEditText = findViewById(R.id.bioEditText)
        profilePicture = findViewById(R.id.personPhotoCircle)
        dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView)

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.menu.findItem(R.id.save).isVisible = true

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Check","1");
            startActivity(intent)
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save -> {
                    savePersonProfileData()
                    true
                }
                else -> { false }
            }
        }

        dateOfBirthLayout.setOnClickListener {
            showDatePicker()
        }

        databaseReference.child(currentUserId).get().addOnSuccessListener {
            val currentUser : Person? = it.getValue(Person::class.java)
            nameEditText.setText(currentUser!!.name)
            nameEditText.hint = ""
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }

    private fun showDatePicker() {

        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.DialogThemeNew,
            { view, year, monthOfYear, dayOfMonth ->

                date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                dateOfBirthTextView.text = date
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun savePersonProfileData() {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val dateOfBirth = date
        val profession = professionEditText.text.toString()
        val township = townshipEditText.text.toString()
        val bio = bioEditText.text.toString()


        val newUser: HashMap<String, String> = HashMap()
        newUser["name"] = name
        newUser["surname"] = surname
        newUser["dateOfBirth"] = dateOfBirth
        newUser["profession"] = profession
        newUser["township"] = township
        newUser["bio"] = bio

        databaseReference.child(currentUserId).updateChildren(newUser as Map<String, Any>)
        finish()
    }
}