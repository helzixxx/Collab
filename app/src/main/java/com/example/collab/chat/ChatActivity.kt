package com.example.collab.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.collab.R
import com.example.collab.models.Match

class ChatActivity : AppCompatActivity() {

    private lateinit var matchUser : Match
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                matchUser = getParcelable("user")!!
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = matchUser.name

    }
}