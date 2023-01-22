package com.example.collab.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.adapters.ChatListAdapter
import com.example.collab.models.Match
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ChatListFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    lateinit var currentUserId: String
    private var matchesList = ArrayList<Match>()
    private lateinit var chatListAdapter : ChatListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val rootView: View = inflater.inflate(R.layout.fragment_chat_list, container, false)

        val auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        currentUserId = auth.currentUser!!.uid

        //region toolbar
        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.search).isVisible = true
        toolbar.menu.findItem(R.id.filter).isVisible = false
        toolbar.menu.findItem(R.id.settings).isVisible = false
        //endregion

        matchesList.clear()
        getMatches()

        val chatListRV: RecyclerView = rootView.findViewById(R.id.chatListRV)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        chatListAdapter = ChatListAdapter(requireContext(), ArrayList())
        chatListRV.layoutManager = layoutManager
        chatListRV.adapter = chatListAdapter

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
                    //var profileImageUrl = ""
                    if (snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }

                    val match = Match(userId, name, "", null)
                    if(!matchesList.contains(match)){
                        matchesList.add(match)
                    }
                    chatListAdapter.updateList(matchesList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    companion object {
        fun newInstance(): ChatListFragment {
            return ChatListFragment()
        }
    }

}

