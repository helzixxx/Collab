package com.example.collab.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.adapters.ChatAdapter
import com.example.collab.models.Message
import com.example.collab.models.Match
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ChatActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var databaseChat: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var currentUserId: String
    private lateinit var chatId: String
    private lateinit var matchUser : Match
    private var messagesList = ArrayList<Message>()
    lateinit var chatAdapter : ChatAdapter
    private lateinit var messageEditText : EditText

    private val resultChat: ArrayList<Message> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        context = this

        val auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        currentUserId = auth.currentUser!!.uid

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                matchUser = getParcelable("user")!!
            }
        }

        databaseUser = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId).child("connections").child("matches").child(matchUser.userId!!).child("ChatId")
        databaseChat = FirebaseDatabase.getInstance().reference.child("Chat")

        getChatId()

        //region toolbar
        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = matchUser.name
        toolbar.setNavigationOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Check", "2")
            startActivity(intent)
        }
        //endregion

        val chatRV: RecyclerView = findViewById(R.id.chatRV)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(context, getChatData())
        chatRV.layoutManager = layoutManager
        chatRV.adapter = chatAdapter

        messageEditText = findViewById(R.id.messageEditText)
        val sendButton = findViewById<ImageView>(R.id.sendButton)
        sendButton.setOnClickListener {
            sendMessage()
        }

    }

    private fun getChatId() {
        databaseUser.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    chatId = snapshot.value.toString()
                    databaseChat = databaseChat.child(chatId)
                    getChatMessages()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getChatMessages() {
        databaseChat.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    var message: String? = null
                    var createdByUser: String? = null
                    var createdAt: String? = null
                    if (snapshot.child("text").value != null) {
                        message = snapshot.child("text").value.toString()
                    }
                    if (snapshot.child("createdByUser").value != null) {
                        createdByUser = snapshot.child("createdByUser").value.toString()
                    }
                    if (snapshot.child("createdAt").value != null) {
                        createdAt = snapshot.child("createdAt").value.toString()
                    }
                    if (message != null && createdByUser != null && createdAt != null) {
                        var currentUserBoolean = false
                        if (createdByUser == currentUserId) {
                            currentUserBoolean = true
                        }
                        val newMessage = Message(message, currentUserBoolean, createdAt)
                        resultChat.add(newMessage)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun sendMessage() {

        val sendMessageText: String = messageEditText.text.toString()

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val current = LocalDateTime.now().format(formatter)

        if (sendMessageText.isNotEmpty()) {
            val newMessageDb: DatabaseReference = databaseChat.push()
            val newMessage: HashMap<String, String> = HashMap()

            newMessage["createdByUser"] = currentUserId
            newMessage["createdAt"] = current
            newMessage["text"] = sendMessageText
            newMessageDb.setValue(newMessage)

            databaseReference.child("Users").child(matchUser.userId!!).child("connections").child("matches").child(currentUserId).child("lastMessage").setValue(sendMessageText)
            databaseReference.child("Users").child(currentUserId).child("connections").child("matches").child(matchUser.userId!!).child("lastMessage").setValue(sendMessageText)

        }
        messageEditText.setText("")
    }

    private fun getChatData(): ArrayList<Message> {
        return resultChat
    }
}