package com.example.collab.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collab.R
import com.example.collab.models.Message
import com.example.collab.helpers.OnItemClickListener
import com.example.collab.helpers.OnItemClickListenerSecond

private const val VIEW_TYPE_MESSAGE_SENT = 1
private const val VIEW_TYPE_MESSAGE_RECEIVED = 2

class ChatAdapter constructor(context : Context, entries: ArrayList<Message>, onItemClickListener: OnItemClickListener<Message>, onItemClickListenerSecond: OnItemClickListenerSecond<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    var context : Context? = null
    private var entries: ArrayList<Message> = java.util.ArrayList()
    var onItemClickListener: OnItemClickListener<Message>
    var onItemClickListenerSecond: OnItemClickListenerSecond<Message>


    init {
        this.context = context
        this.entries = entries
        this.onItemClickListener = onItemClickListener
        this.onItemClickListenerSecond = onItemClickListenerSecond
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

        private var card_message_me_music: CardView = view.findViewById(R.id.card_message_me_music)
        private var music_name: TextView = view.findViewById(R.id.music_name)
        private var timestamp_me_music: TextView = view.findViewById(R.id.timestamp_me_music)
        private var play: ImageView = view.findViewById(R.id.play)
        private var pause: ImageView = view.findViewById(R.id.pause)
        private var card_chat_message_me: CardView = view.findViewById(R.id.card_chat_message_me)
        private var timestamp_me: TextView = view.findViewById(R.id.timestamp_me)
        private var messageText: TextView = view.findViewById(R.id.message_me)
        private var timeText: TextView = view.findViewById(R.id.timestamp_me)
        private var image_me: ImageView = view.findViewById(R.id.image_me)
        private var timestamp_me_image: TextView = view.findViewById(R.id.timestamp_me_image)

        override fun bind(chat: Message) {
            if(chat.imageUrl != null && chat.imageUrl != ""){
                card_chat_message_me.visibility = View.GONE
                timestamp_me.visibility = View.GONE
                image_me.visibility = View.VISIBLE
                timestamp_me_image.visibility = View.VISIBLE
                timestamp_me_image.text = chat.createdAt
                Glide.with(context!!).load(chat.imageUrl).into(image_me)
            } else if(chat.audioUrl != null && chat.audioUrl != ""){
                card_chat_message_me.visibility = View.GONE
                timestamp_me.visibility = View.GONE
                image_me.visibility = View.GONE
                card_message_me_music.visibility = View.VISIBLE
                timestamp_me_music.visibility = View.VISIBLE
                timestamp_me_music.text = chat.createdAt
                music_name.text = chat.audioName
                play.setOnClickListener {
                    onItemClickListener.onItemClick(chat)
                }
                pause.setOnClickListener {
                    onItemClickListenerSecond.onItemClick(chat)
                }

            } else {
                messageText.text = chat.message
                timeText.text = chat.createdAt
            }

        }
    }

    inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {

        private var card_message_me_music: CardView = view.findViewById(R.id.card_message_me_music)
        private var music_name: TextView = view.findViewById(R.id.music_name)
        private var timestamp_other_music: TextView = view.findViewById(R.id.timestamp_other_music)
        private var play: ImageView = view.findViewById(R.id.play)
        private var pause: ImageView = view.findViewById(R.id.pause)
        private var message_card: CardView = view.findViewById(R.id.message_card)
        private var timestamp_other: TextView = view.findViewById(R.id.timestamp_other)
        private var messageText: TextView = view.findViewById(R.id.message_other)
        private var timeText: TextView = view.findViewById(R.id.timestamp_other)
        private var image_other: ImageView = view.findViewById(R.id.image_other)
        private var timestamp_other_image: TextView = view.findViewById(R.id.timestamp_other_image)

        override fun bind(chat: Message) {
            if(chat.imageUrl != null && chat.imageUrl != ""){
                message_card.visibility = View.GONE
                timestamp_other.visibility = View.GONE
                image_other.visibility = View.VISIBLE
                timestamp_other_image.visibility = View.VISIBLE
                timestamp_other_image.text = chat.createdAt
                Glide.with(context!!).load(chat.imageUrl).into(image_other)
            } else if(chat.audioUrl != null && chat.audioUrl != ""){
                message_card.visibility = View.GONE
                timestamp_other.visibility = View.GONE
                image_other.visibility = View.GONE
                card_message_me_music.visibility = View.VISIBLE
                timestamp_other_music.visibility = View.VISIBLE
                timestamp_other_music.text = chat.createdAt
                music_name.text = chat.audioName
                play.setOnClickListener {
                    onItemClickListener.onItemClick(chat)
                }
                pause.setOnClickListener {
                    onItemClickListenerSecond.onItemClick(chat)
                }

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