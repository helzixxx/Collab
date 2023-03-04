package com.example.collab.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collab.R
import com.example.collab.models.Message

private const val VIEW_TYPE_MESSAGE_SENT = 1
private const val VIEW_TYPE_MESSAGE_RECEIVED = 2

class ChatAdapter constructor(context : Context, entries: ArrayList<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    var context : Context? = null
    private var entries: ArrayList<Message> = java.util.ArrayList()

    init {
        this.context = context
        this.entries = entries
    }

    fun updateList(entries: ArrayList<Message>){
        this.entries = entries
        notifyDataSetChanged()
    }

    fun addMessage(chat: Message){
        entries.add(chat)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
       // val view: View

        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            MyMessageViewHolder(
                layoutInflater.inflate(R.layout.single_current_user_chat_row, parent, false)
            )
        } else {
            OtherMessageViewHolder(
                layoutInflater.inflate(R.layout.single_other_user_chat_row, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat: Message = entries[position]

        return if (chat.currentUser) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    inner class MyMessageViewHolder (view: View) : MessageViewHolder(view) {

        private var messageText: TextView = view.findViewById(R.id.message_me)
        private var timeText: TextView = view.findViewById(R.id.timestamp_me)
        private var image_me: ImageView = view.findViewById(R.id.image_me)

        override fun bind(chat: Message) {
            if(chat.imageUrl != null && chat.imageUrl != ""){
                messageText.visibility = View.GONE
                image_me.visibility = View.VISIBLE
                Glide.with(context!!).load(chat.imageUrl).into(image_me)
            } else {
                messageText.text = chat.message
                timeText.text = chat.createdAt
            }

        }
    }

    inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {

        private var messageText: TextView = view.findViewById(R.id.message_other)
        private var timeText: TextView = view.findViewById(R.id.timestamp_other)
        private var image_other: ImageView = view.findViewById(R.id.image_other)

        override fun bind(chat: Message) {
            if(chat.imageUrl != null && chat.imageUrl != ""){
                messageText.visibility = View.GONE
                image_other.visibility = View.VISIBLE
                Glide.with(context!!).load(chat.imageUrl).into(image_other)
            } else {
                messageText.text = chat.message
                timeText.text = chat.createdAt
            }
        }
    }

    open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(chat :Message) {}
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val chat = entries[position]
        holder.bind(chat)
    }

}