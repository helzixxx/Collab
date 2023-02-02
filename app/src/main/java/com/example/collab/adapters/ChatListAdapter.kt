package com.example.collab.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.chat.ChatActivity
import com.example.collab.models.Match
import com.google.android.material.imageview.ShapeableImageView

class ChatListAdapter constructor(context : Context, entries: ArrayList<Match>) :
    RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {

    var context : Context? = null
    private var entries: ArrayList<Match> = java.util.ArrayList()

    init {
        this.context = context
        this.entries = entries
    }

    fun updateList(entries: ArrayList<Match>){
        this.entries = entries
        notifyDataSetChanged()
    }

    fun updateListSearch(searchString: CharSequence?){
        updateList(entries.filter { it.name!!.contains(searchString!!,true) } as ArrayList<Match> )
    }

    fun updateListEntries(){
        updateList(entries)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.single_chat_list_row, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val match: Match = entries[position]

        holder.userName.text = match.name
        holder.lastMessage.text = match.lastMessage
        if (match.profileImage != null && match.profileImage != "") {
            Glide.with(context!!).load(match.profileImage).dontTransform().into(holder.personPhotoCircle)
        } else {
            Glide.with(context!!).load(R.drawable.defaut_profile_image).into(holder.personPhotoCircle)
        }

        holder.container.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("user", match)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView
        var lastMessage: TextView
        var personPhotoCircle: ShapeableImageView
        var container: CardView

        init {
            userName = itemView.findViewById(R.id.userName)
            lastMessage = itemView.findViewById(R.id.lastMessage)
            personPhotoCircle = itemView.findViewById(R.id.personPhotoCircle)
            container = itemView.findViewById(R.id.container)
        }
    }

}