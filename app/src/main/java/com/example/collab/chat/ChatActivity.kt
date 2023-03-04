package com.example.collab.chat

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException
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
    private lateinit var matchUser: Match
    private var messagesList = ArrayList<Message>()
    lateinit var chatAdapter: ChatAdapter
    private lateinit var messageEditText: EditText

    private var imageUri: Uri? = null
    private val pickImage = 100

    private val resultChat: ArrayList<Message> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
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

        databaseUser = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId)
            .child("connections").child("matches").child(matchUser.userId!!).child("ChatId")
        databaseChat = FirebaseDatabase.getInstance().reference.child("Chat")

        getChatId()

        //region toolbar
        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = matchUser.name
        toolbar.setNavigationOnClickListener {
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

        val attachButton = findViewById<ImageView>(R.id.attachButton)
        attachButton.setOnClickListener {
            showAttachDialog()
        }


    }



    private fun getChatId() {
        databaseUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
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
        databaseChat.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    var message: String? = null
                    var createdByUser: String? = null
                    var createdAt: String? = null
                    var imageUrl: String? = null
                    if (snapshot.child("text").value != null) {
                        message = snapshot.child("text").value.toString()
                    }
                    if (snapshot.child("createdByUser").value != null) {
                        createdByUser = snapshot.child("createdByUser").value.toString()
                    }
                    if (snapshot.child("createdAt").value != null) {
                        createdAt = snapshot.child("createdAt").value.toString()
                    }
                    if (snapshot.child("imageUrl").value != null) {
                        imageUrl = snapshot.child("imageUrl").value.toString()
                    }
                    if(imageUrl != null && createdByUser != null && createdAt != null){
                        var currentUserBoolean = false
                        if (createdByUser == currentUserId) {
                            currentUserBoolean = true
                        }
                        val newMessage = Message("", imageUrl, currentUserBoolean, createdAt)
                        resultChat.add(newMessage)
                        chatAdapter.notifyDataSetChanged()
                    } else if (message != null && createdByUser != null && createdAt != null) {
                        var currentUserBoolean = false
                        if (createdByUser == currentUserId) {
                            currentUserBoolean = true
                        }
                        val newMessage = Message(message, "", currentUserBoolean, createdAt)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

            databaseReference.child("Users").child(matchUser.userId!!).child("connections")
                .child("matches").child(currentUserId).child("lastMessage")
                .setValue(sendMessageText)
            databaseReference.child("Users").child(currentUserId).child("connections")
                .child("matches").child(matchUser.userId!!).child("lastMessage")
                .setValue(sendMessageText)

        }
        messageEditText.setText("")
    }

    private fun showAttachDialog() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choose_attach_content)

        val lWindowParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(dialog.window!!.attributes)
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lWindowParams

        val imageTextView = dialog.findViewById<TextView>(R.id.imageTextView)
        val fileTextView = dialog.findViewById<TextView>(R.id.fileTextView)
        val musicTextView = dialog.findViewById<TextView>(R.id.musicTextView)

        imageTextView.setOnClickListener {
           //gallery
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
            dialog.dismiss()
        }

        fileTextView.setOnClickListener {
            //file
        }

        musicTextView.setOnClickListener {
            //music
        }

        dialog.setCancelable(true)
        dialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendImageMessage() {

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val current = LocalDateTime.now().format(formatter)

        storageReference = FirebaseStorage.getInstance().getReference("Chat/$chatId/$current")

        var bitmap: Bitmap? = null

        try {
            bitmap = MediaStore.Images.Media.getBitmap(application.contentResolver, imageUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data: ByteArray = baos.toByteArray()
        val uploadTask: UploadTask = storageReference.putBytes(data)
        uploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.storage.downloadUrl.addOnSuccessListener {

                    val newMessageDb: DatabaseReference = databaseChat.push()
                    val newMessage: HashMap<String, String> = HashMap()

                    newMessage["createdByUser"] = currentUserId
                    newMessage["createdAt"] = current
                    newMessage["imageUrl"] = it.toString()
                    newMessageDb.setValue(newMessage)

//                    databaseReference.child("Users").child(currentUserId).child("profileImage")
//                        .setValue(it.toString()).addOnSuccessListener {
//                            Log.e("", "Pic uploaded successfully to database")
//
//                        }.addOnFailureListener {
//                            Log.e("", "Pic upload failed to database : $it")
//                        }
                }.addOnFailureListener {
                    Log.e("", "Pic downloadUrl failed : $it")
                }

                finish()
                Log.e("", "Pic uploaded successfully to storage")
            }


        }.addOnFailureListener {
            Log.e("", "Pic uploaded failed to storage")
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data!!.data!!
            sendImageMessage()
        }
    }

    private fun getChatData(): ArrayList<Message> {
        return resultChat
    }
}