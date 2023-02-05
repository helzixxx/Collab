package com.example.collab.profile

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.dialogs.SelectGenresDialog
import com.example.collab.dialogs.SelectInstrumentsDialog
import com.example.collab.models.Genre
import com.example.collab.models.Instrument
import com.example.collab.models.Person
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException
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
    private lateinit var genresTextView: TextView
    private lateinit var instrumentsTextView: TextView
    private lateinit var profilePicture: ShapeableImageView

    var date: String = ""
    private lateinit var currentUserId: String
    private var imageUri: Uri? = null
    private val pickImage = 100

    private  var genres: ArrayList<Genre?>? = null
    private  var instruments: ArrayList<Instrument?>? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        context = this

        val auth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference

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
        genresTextView = findViewById(R.id.genresTextView)
        instrumentsTextView = findViewById(R.id.instrumentsTextView)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.genres.observe(this) {
            if (it != null) {
                genres = it
            }
        }

        viewModel.instruments.observe(this) {
            if (it != null) {
                instruments = it
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.menu.findItem(R.id.save).isVisible = true

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Check", "1");
            startActivity(intent)
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save -> {
                    savePersonProfileData()
                    true
                }
                else -> {
                    false
                }
            }
        }



        profilePicture.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        dateOfBirthLayout.setOnClickListener {
            showDatePicker()
        }

        genresLayout.setOnClickListener {
            showSelectGenresDialog()
        }

        instrumentsLayout.setOnClickListener {
            showSelectInstrumentsDialog()
        }

        databaseReference.child("Users").child(currentUserId).get().addOnSuccessListener {
            val currentUser: Person? = it.getValue(Person::class.java)
            nameEditText.setText(currentUser!!.name)
            surnameEditText.setText(currentUser.surname)
            surnameEditText.hint = ""
            dateOfBirthTextView.text = currentUser.dateOfBirth
            professionEditText.setText(currentUser.profession)
            professionEditText.hint = ""
            townshipEditText.setText(currentUser.township)
            townshipEditText.hint = ""
            bioEditText.setText(currentUser.bio)
            bioEditText.hint = ""

            if(currentUser.genres != null){
                val genreArrayList : ArrayList<String?> = ArrayList()
                currentUser.genres!!.forEach { genre ->
                    genreArrayList += genre!!.name
                }
                val genresString = genreArrayList.joinToString()
                genresTextView.text = genresString
            }

            if(currentUser.instruments != null){
                val instrumentArrayList : ArrayList<String?> = ArrayList()
                currentUser.instruments!!.forEach { instrument ->
                    instrumentArrayList += instrument!!.name
                }
                val instrumentsString = instrumentArrayList.joinToString()
                instrumentsTextView.text = instrumentsString
            }

            if (currentUser.profileImage != "" && currentUser.profileImage != null) {
                Glide.with(context).load(currentUser.profileImage).into(profilePicture)
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }

//        databaseReference.child("Genres").get().addOnSuccessListener {
//
//            for (productSnapshot in it.children) {
//                val genre: Genre? = productSnapshot.getValue(Genre::class.java)
//                genres.add(genre)
//            }
//            Log.e("firebase", "Successfully got data $genres")
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }

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


        //todo при создании профиля и сразу же при загрузке данных и выбора
        // профильной фотографии происходит ошибка и фаербейз не загружет ее в хранилище

        val newPerson = Person(name, surname, dateOfBirth, profession, township, "", bio, genres, instruments)
        databaseReference.child("Users").child(currentUserId).setValue(newPerson)
            .addOnSuccessListener {
                Toast.makeText(context, "user data uploaded successfully", Toast.LENGTH_SHORT)
                    .show()
                if (imageUri != null) {
                    uploadProfilePicture()
                }
            }.addOnFailureListener {
            Toast.makeText(context, "user data upload failed ", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun uploadProfilePicture() {

        storageReference = FirebaseStorage.getInstance().getReference("Users/$currentUserId")
//            storageReference.putFile(imageUri!!).addOnSuccessListener {
//                Toast.makeText(context, "Pic uploaded successfully", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(context, "Pic upload failed ", Toast.LENGTH_SHORT).show()
//            }
        var bitmap: Bitmap? = null

        try {
            bitmap = MediaStore.Images.Media.getBitmap(application.contentResolver, imageUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data: ByteArray = baos.toByteArray()
        val uploadTask: UploadTask = storageReference.putBytes(data)
        uploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.storage.downloadUrl.addOnSuccessListener {
                    databaseReference.child("Users").child(currentUserId).child("profileImage")
                        .setValue(it.toString()).addOnSuccessListener {
                            Log.e("", "Pic uploaded successfully to database")

                        }.addOnFailureListener {
                            Log.e("", "Pic upload failed to database : $it")
                        }
                }.addOnFailureListener {
                    Log.e("", "Pic downloadUrl failed : $it")
                }

                finish()
                Log.e("", "Pic uploaded successfully to storage")
            }


        }.addOnFailureListener {
            Log.e("", "Pic uploaded failed to storage")
        }


    }

    private fun downloadProfileImage() {
        storageReference = FirebaseStorage.getInstance().getReference("Users/$currentUserId")
        storageReference.downloadUrl.addOnSuccessListener {
            Glide.with(this@EditProfileActivity)
                .load(it)
                .into(profilePicture)
            Toast.makeText(context, "Pic downloaded successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Pic download failed ", Toast.LENGTH_SHORT).show()
        }
    }

//    fun showGenreListDialog(view: View) {
//
//        val items = arrayOf("Microsoft", "Apple", "Amazon", "Google")
//        val selectedList = ArrayList<Int>()
//        val builder = AlertDialog.Builder(this)
//
//        builder.setTitle("This is list choice dialog box")
//        builder.setMultiChoiceItems(items, null
//        ) { dialog, which, isChecked ->
//            if (isChecked) {
//                selectedList.add(which)
//            } else if (selectedList.contains(which)) {
//                selectedList.remove(Integer.valueOf(which))
//            }
//        }
//
//        builder.setPositiveButton("DONE") { dialogInterface, i ->
//            val selectedStrings = ArrayList<String>()
//
//            for (j in selectedList.indices) {
//                selectedStrings.add(items[selectedList[j]])
//            }
//
//            Toast.makeText(applicationContext, "Items selected are: " + Arrays.toString(selectedStrings.toTypedArray()), Toast.LENGTH_SHORT).show()
//        }
//
//        builder.show()
//
//    }

    private fun showSelectGenresDialog() {
        val fragmentManager = supportFragmentManager
        val selectGenresDialog = SelectGenresDialog(false)
        selectGenresDialog.show(fragmentManager, "selectGenresDialogFragment")
    }

    private fun showSelectInstrumentsDialog() {
        val fragmentManager = supportFragmentManager
        val selectInstrumentsDialog = SelectInstrumentsDialog(false)
        selectInstrumentsDialog.show(fragmentManager, "selectInstrumentsDialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data!!.data!!
            profilePicture.setImageURI(imageUri)
        }
    }

}