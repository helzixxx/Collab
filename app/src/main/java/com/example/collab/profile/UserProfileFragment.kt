package com.example.collab.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.SettingsActivity
import com.example.collab.cards.CardsFragment
import com.example.collab.models.Person
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val ARG_PARAM1 = "UserId"

class UserProfileFragment : Fragment() {

    lateinit var currentUserId : String
    val currentUser : Person? = null

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

        val editProfile = view.findViewById<ImageView>(R.id.edit_profile)
        editProfile.setOnClickListener {
            val intent = Intent(requireActivity() as MainActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val auth = FirebaseAuth.getInstance()

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Users")

        currentUserId = auth.currentUser!!.uid

        //todo нажатие бэк стрелки когда выходишь из настроек или эдит профайла триггерит нул эксепшн
        // потому что в интент нет значения
        databaseReference.child(currentUserId).get().addOnSuccessListener {
            val currentUser : Person? = it.getValue(Person::class.java)
            val name = view.findViewById<TextView>(R.id.name)
            name.text = currentUser!!.name
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        return view
    }


    companion object {
            fun newInstance(): UserProfileFragment {
                return UserProfileFragment()
            }
    }
}