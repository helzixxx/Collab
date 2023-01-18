package com.example.collab.cards


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.adapters.CardsAdapter
import com.example.collab.models.Card
import com.example.collab.models.Person
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lorentzos.flingswipe.SwipeFlingAdapterView


class CardsFragment : Fragment() {

    private lateinit var usersCards: ArrayList<Card>

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    lateinit var currentUserId: String

    private lateinit var cardFrame: SwipeFlingAdapterView
    private lateinit var noMoreMatches: FrameLayout
//    private lateinit var likeButton: ImageView
//    private lateinit var dislikeButton: ImageView

    var cardsAdapter: CardsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_profile_card, container, false)

        val auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        currentUserId = auth.currentUser!!.uid

        //region toolbar
        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.filter).isVisible = true
        toolbar.menu.findItem(R.id.search).isVisible = false
        toolbar.menu.findItem(R.id.settings).isVisible = false
        //endregion

        cardFrame = rootView.findViewById(R.id.cardFrame)
        noMoreMatches = rootView.findViewById(R.id.noMoreMatches)

        getCardUsers()
        usersCards = ArrayList()

        cardsAdapter = CardsAdapter(requireContext(), R.layout.card_item,  usersCards)
        cardFrame.adapter = cardsAdapter
        cardFrame.setFlingListener(object: SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {

            }

            override fun onLeftCardExit(p0: Any?) {

            }

            override fun onRightCardExit(p0: Any?) {

            }

            override fun onAdapterAboutToEmpty(p0: Int) {
            }

            override fun onScroll(p0: Float) {

            }

        })

        cardFrame.setOnItemClickListener { itemPosition, dataObject ->
            //todo user info
        }


        return rootView
    }

//    private fun checkRowItem() {
//        if (usersCards.isEmpty()) {
//            noMoreMatches.visibility = View.VISIBLE
//            cardFrame.visibility = View.GONE
//        }
//    }


    private fun getCardUsers() {
        databaseReference.child("Users").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataSnapshot.key != currentUserId) {
                    val card = Card(
                        dataSnapshot.key!!,
                        dataSnapshot.child("name").value.toString(),
                        dataSnapshot.child("profession").value.toString(),
                        ""
                    )
                    usersCards.add(card)
                    cardsAdapter!!.notifyDataSetChanged()


                    Log.e("firebase", "Successfully got data $usersCards")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    companion object {
        fun newInstance(): CardsFragment {
            return CardsFragment()
        }
    }

}