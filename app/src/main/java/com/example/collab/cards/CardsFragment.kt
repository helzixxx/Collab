package com.example.collab.cards


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.SettingsActivity
import com.example.collab.adapters.CardsAdapter
import com.example.collab.models.Card
import com.example.collab.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lorentzos.flingswipe.SwipeFlingAdapterView


class CardsFragment : Fragment() {

    private lateinit var usersCards: ArrayList<Card?>

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

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.filter -> {
                    showFilterDialog()
                    true
                }
                else -> { false }
            }
        }
        //endregion

        cardFrame = rootView.findViewById(R.id.cardFrame)
        noMoreMatches = rootView.findViewById(R.id.noMoreMatches)

        getCardUsers()

        usersCards = ArrayList()

        cardsAdapter = CardsAdapter(requireContext(), R.layout.card_item,  usersCards)
        cardFrame.adapter = cardsAdapter

        cardFrame.setFlingListener(object: SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                usersCards.removeAt(0);
                cardsAdapter!!.notifyDataSetChanged();
            }

            override fun onLeftCardExit(p0: Any?) {
                val card: Card = p0 as Card
                databaseReference.child("Users").child(card.userId!!).child("connections").child("dislike").child(currentUserId).setValue(true)
                checkRowItem()
            }

            override fun onRightCardExit(p0: Any?) {
                val card: Card = p0 as Card
                databaseReference.child("Users").child(card.userId!!).child("connections").child("like").child(currentUserId).setValue(true)
                isMatchHappened(card.userId!!)
                checkRowItem()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
            }

            override fun onScroll(p0: Float) {

            }

        })

        cardFrame.setOnItemClickListener { itemPosition, dataObject ->
            val card: Card? = dataObject as Card?
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("cardUserId", card!!.userId)
            startActivity(intent)
        }


        return rootView
    }

    private fun showFilterDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_filter_user_cards)

        val lWindowParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(dialog.window!!.attributes)
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lWindowParams

        val spinnerGenre = dialog.findViewById<LinearLayout>(R.id.spinnerGenre)
        val spinnerInstrument = dialog.findViewById<LinearLayout>(R.id.spinnerInstrument)
        val imageViewDone = dialog.findViewById<ImageView>(R.id.imageViewDone)
        val imageViewClose = dialog.findViewById<ImageView>(R.id.imageViewClose)


        spinnerGenre.setOnClickListener {

        }

        spinnerInstrument.setOnClickListener {

        }

        imageViewDone.setOnClickListener {

        }

        imageViewClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun checkRowItem() {
        if (usersCards.isEmpty()) {
            noMoreMatches.visibility = View.VISIBLE
            cardFrame.visibility = View.GONE
        }
    }


    private fun getCardUsers() {
        databaseReference.child("Users").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataSnapshot.exists() &&
                    dataSnapshot.key != currentUserId  &&
                    !dataSnapshot.child("connections").child("dislike").hasChild(currentUserId) &&
                    !dataSnapshot.child("connections").child("like").hasChild(currentUserId)) {
                    val card = Card(
                        dataSnapshot.key!!,
                        dataSnapshot.child("name").value.toString(),
                        dataSnapshot.child("profession").value.toString(),
                        dataSnapshot.child("profileImage").value.toString()
                    )
                    usersCards.add(card)
                    cardsAdapter!!.notifyDataSetChanged()
                    Log.e("firebase", "Successfully got data $usersCards")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    fun isMatchHappened( userId : String ){
        val currentUserConnection: DatabaseReference =
            databaseReference.child("Users").child(currentUserId).child("connections").child("like").child(userId)
        currentUserConnection.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(requireActivity(), "new Connection", Toast.LENGTH_LONG).show()

                    val key = databaseReference.child("Chat").push().key

                    databaseReference.child("Users").child(snapshot.key!!).child("connections")
                        .child("matches").child(currentUserId).child("ChatId").setValue(key)
                    databaseReference.child("Users").child(currentUserId).child("connections")
                        .child("matches").child(snapshot.key!!).child("ChatId").setValue(key)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    companion object {
        fun newInstance(): CardsFragment {
            return CardsFragment()
        }
    }

}