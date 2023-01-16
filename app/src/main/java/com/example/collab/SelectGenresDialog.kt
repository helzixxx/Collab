package com.example.collab

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.adapters.GenreCheckBoxListAdapter
import com.example.collab.models.Genre
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SelectGenresDialog: DialogFragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var genres : ArrayList<Genre?>
    var adapter: GenreCheckBoxListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.dialog_select_genre, container, false)

        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Genres").get().addOnSuccessListener {

            genres = ArrayList<Genre?>()
            var genre: Genre?
            for (productSnapshot in it.children) {
                genre = productSnapshot.getValue(Genre::class.java)
                genres.add(genre)
            }

            val genreListRecyclerView: RecyclerView = rootView.findViewById(R.id.searched_genres_list)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            adapter = GenreCheckBoxListAdapter(requireContext(), genres!!)
            genreListRecyclerView.adapter = adapter
            genreListRecyclerView.layoutManager  = layoutManager
            Log.e("firebase", "Successfully got data $genres")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }





        return rootView
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}