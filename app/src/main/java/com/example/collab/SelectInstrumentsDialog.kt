package com.example.collab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.adapters.GenreCheckBoxListAdapter
import com.example.collab.adapters.InstrumentCheckBoxListAdapter
import com.example.collab.models.Genre
import com.example.collab.models.Instrument
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SelectInstrumentsDialog: DialogFragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var instruments : ArrayList<Instrument?>
    var adapter: InstrumentCheckBoxListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.dialog_select_genre_or_instrument, container, false)

        val title = rootView.findViewById<TextView>(R.id.custom_spinner_title)
        title.text = "Wybierz instrumenty"

        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Instruments").get().addOnSuccessListener {

            instruments = ArrayList<Instrument?>()
            var instrument: Instrument?
            for (productSnapshot in it.children) {
                instrument = productSnapshot.getValue(Instrument::class.java)
                instruments.add(instrument)
            }

            val genreListRecyclerView: RecyclerView = rootView.findViewById(R.id.searched_genres_list)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            adapter = InstrumentCheckBoxListAdapter(requireContext(), instruments)
            genreListRecyclerView.adapter = adapter
            genreListRecyclerView.layoutManager  = layoutManager
            Log.e("firebase", "instruments:  $instruments")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting instruments : ", it)
        }

        val genreButton = rootView.findViewById<Button>(R.id.genreButton)
        genreButton.setOnClickListener {

        }

        return rootView
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}