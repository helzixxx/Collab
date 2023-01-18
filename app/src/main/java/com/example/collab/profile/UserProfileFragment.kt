package com.example.collab.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.SettingsActivity
import com.example.collab.cards.CardsFragment
import com.example.collab.models.Person
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val ARG_PARAM1 = "UserId"

class UserProfileFragment : Fragment() {

    var currentUser : Person? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    lateinit var currentUserId : String
    private lateinit var imageUri : Uri

    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var dateOfBirth: TextView
    private lateinit var profession: TextView
    private lateinit var instruments: TextView
    private lateinit var genres: TextView
    private lateinit var township: TextView
    private lateinit var bio: TextView
    private lateinit var profilePicture: ShapeableImageView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.settings).isVisible = true
        toolbar.menu.findItem(R.id.search).isVisible = false
        toolbar.menu.findItem(R.id.filter).isVisible = false

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    val intent = Intent(requireActivity() as MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> { false }
            }
        }


        name = view.findViewById(R.id.name)
        surname = view.findViewById(R.id.surnameTextView)
        dateOfBirth = view.findViewById(R.id.dateOfBirthTextView)
        bio = view.findViewById(R.id.bioTextView)
        profession = view.findViewById(R.id.professionTextView)
        instruments = view.findViewById(R.id.instrumentsTextView)
        genres = view.findViewById(R.id.genresTextView)
        township = view.findViewById(R.id.townshipTextView)
        profilePicture = view.findViewById(R.id.personPhotoCircle)

        val editProfile = view.findViewById<ImageView>(R.id.edit_profile)
        editProfile.setOnClickListener {
            val intent = Intent(requireActivity() as MainActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val auth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        currentUserId = auth.currentUser!!.uid

        // todo потянуть вниз для обновления информации о юзере
        // сделать функцию getUserInfo и запихнуть туда
        databaseReference.child(currentUserId).get().addOnSuccessListener {
            currentUser = it.getValue(Person::class.java)
            name.text = currentUser!!.name
            surname.text = currentUser!!.surname
            dateOfBirth.text = currentUser!!.dateOfBirth
            bio.text = currentUser!!.bio
            profession.text = currentUser!!.profession
            township.text = currentUser!!.township
            downloadProfileImage()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        return view
    }

    private fun downloadProfileImage(){
        storageReference = FirebaseStorage.getInstance().getReference("Users/$currentUserId")
        storageReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireActivity())
                .load(it)
                .into(profilePicture)
            Toast.makeText(context, "Pic downloaded successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Pic download failed ", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
            fun newInstance(): UserProfileFragment {
                return UserProfileFragment()
            }
    }
}