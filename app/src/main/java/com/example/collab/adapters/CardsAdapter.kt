package com.example.collab.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.collab.R
import com.example.collab.models.Card
import com.google.firebase.storage.FirebaseStorage

class CardsAdapter constructor(
    context: Context,
    layoutResource: Int,
    private val entries: ArrayList<Card?>
) :
    ArrayAdapter<Card>(context, layoutResource, entries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val card: Card? = entries[position]
        var view = convertView

        if (view == null) {
            val layoutInflater = LayoutInflater.from(parent!!.context)
            view = layoutInflater.inflate(R.layout.card_item, parent, false)
        }

//        var profileCardLayout =  view.findViewById(R.id.profileCardLayout)
        var personPhotoCard = view!!.findViewById<ImageView>(R.id.personPhotoCard)
//        var likeButton = view.findViewById(R.id.likeButton)
//        var dislikeButton = view.findViewById(R.id.dislikeButton)
        val user_name = view!!.findViewById<TextView>(R.id.user_name)
        val user_bio = view.findViewById<TextView>(R.id.user_bio)

        user_name.text = card!!.name
        user_bio.text = card.profession

        if (card.profileImage != null && card.profileImage != "") {
            Glide.with(context).load(card.profileImage).into(personPhotoCard)
        } else {
            Glide.with(context).load(R.drawable.defaut_profile_image).into(personPhotoCard)
        }


        return view
    }

}