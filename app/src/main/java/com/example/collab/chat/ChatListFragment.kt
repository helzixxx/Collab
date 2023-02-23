package com.example.collab.chat

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.SettingsActivity
import com.example.collab.adapters.ChatListAdapter
import com.example.collab.models.Genre
import com.example.collab.models.Match
import com.example.collab.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class ChatListFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    lateinit var currentUserId: String
    private var matchesList = ArrayList<Match>()
    lateinit var chatListAdapter : ChatListAdapter
    private var containerSearchView: View? = null
    var isDown: Boolean = false
    private var listView: LinearLayout? = null

    private lateinit var viewModel: ChatListViewModel
    private var chats = ArrayList<Match>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val rootView: View = inflater.inflate(R.layout.fragment_chat_list, container, false)

        val auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        currentUserId = auth.currentUser!!.uid

        listView = rootView.findViewById(R.id.containerMatch)
        containerSearchView = rootView.findViewById(R.id.containerSearch)
        containerSearchView?.visibility = View.GONE

        //region toolbar
        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.search).isVisible = true
        toolbar.menu.findItem(R.id.filter).isVisible = false
        toolbar.menu.findItem(R.id.settings).isVisible = false

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.search -> {
                    if (isDown) hide()
                    else show()
                    true
                }
                else -> { false }
            }
        }

        //endregion

        viewModel = ViewModelProvider(requireActivity() as MainActivity)[ChatListViewModel::class.java]
        viewModel.chats.observe(requireActivity()) {
            try {
                if (it != null){
                    chats = it
                }
            } catch ( e: IOException) {
                Log.e("ERROR", " ERROR getPlannedOperations" )
            }
        }

        matchesList.clear()
        getMatches()

        val chatListRV: RecyclerView = rootView.findViewById(R.id.chatListRV)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        chatListAdapter = ChatListAdapter(requireContext(), ArrayList())
        chatListRV.layoutManager = layoutManager
        chatListRV.adapter = chatListAdapter

        val editTextSearch = rootView.findViewById<EditText>(R.id.editTextName)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0 != null && p0 != ""){
                    chatListAdapter.updateList(viewModel.chats.value!!.filter { it.name!!.contains(
                        p0,true)} as ArrayList<Match>)
                } else {
                    chatListAdapter.updateList(viewModel.chats.value!!)
                }

            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        return rootView
    }

    private fun getMatches() {
        val matchDb = databaseReference
            .child("Users")
            .child(currentUserId)
            .child("connections")
            .child("matches")
        matchDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (match in snapshot.children) {
                        fetchMatchesInformation(match.key)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun fetchMatchesInformation(key: String?) {
        val userDb = databaseReference.child("Users").child(key!!)
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userId: String = snapshot.key!!
                    var name = ""
                    var profileImageUrl = ""
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }
                    if (snapshot.child("profileImage").value != null) {
                        profileImageUrl = snapshot.child("profileImage").value.toString()
                    }

                    val match = Match(userId, name, "", profileImageUrl)
                    if(!matchesList.contains(match)){
                        matchesList.add(match)
                    }
                    viewModel.chats.value = matchesList
                    chatListAdapter.updateList(matchesList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun hide() {
        containerSearchView!!.visibility = View.GONE
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 112, 0, 0)
        listView!!.layoutParams = params
        isDown = false
    }

    fun show(){
        containerSearchView!!.visibility = View.VISIBLE
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 224, 0, 0)
        listView!!.layoutParams = params
        isDown = true
    }

    companion object {
        fun newInstance(): ChatListFragment {
            return ChatListFragment()
        }
    }

}

