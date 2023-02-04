package com.example.collab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.adapters.GenreCheckBoxListAdapter
import com.example.collab.adapters.OnItemClickListener
import com.example.collab.models.Genre
import com.example.collab.models.Instrument
import com.example.collab.profile.EditProfileActivity
import com.example.collab.profile.ProfileViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SelectGenresDialog: DialogFragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var genres : ArrayList<Genre?>
    var adapter: GenreCheckBoxListAdapter? = null
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.dialog_select_genre_or_instrument, container, false)

        viewModel = ViewModelProvider(requireActivity() as EditProfileActivity)[ProfileViewModel::class.java]
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
            adapter = GenreCheckBoxListAdapter(requireContext(), genres, object :
                OnItemClickListener<Genre?> {
                override fun onItemClick(item: Genre?) {
                    genres.forEach { inst ->
                        if (inst!!.id == item!!.id){
                            inst.isChecked = !inst.isChecked
                        }
                    }
                    //item!!.isChecked = !item.isChecked
                }
            })
            genreListRecyclerView.adapter = adapter
            genreListRecyclerView.layoutManager  = layoutManager
            Log.e("firebase", "Successfully got data $genres")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        val genreButton = rootView.findViewById<Button>(R.id.genreButton)
        genreButton.setOnClickListener {
            viewModel.genres.value = genres.filter { it!!.isChecked } as ArrayList<Genre?>
            dialog!!.cancel()
        }

        return rootView
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}