package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.collab.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val auth = FirebaseAuth.getInstance()


        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Check","1")
            startActivity(intent)
        }

        val logOutBtn = findViewById<Button>(R.id.logOut)
        logOutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}