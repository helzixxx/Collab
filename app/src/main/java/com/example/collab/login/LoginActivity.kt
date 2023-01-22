package com.example.collab.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collab.MainActivity
import com.example.collab.R
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var context: Context

    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)

        auth = FirebaseAuth.getInstance()

        val redirectSignUp: TextView = findViewById(R.id.redirectSignUp)
        val loginButton: TextView = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            logIn()
        }

        redirectSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn() {

        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email i Hasło nie mogą być puste", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isComplete && task.isSuccessful) {
                Toast.makeText(this, "Jesteś zalogowany", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("UserId", auth.currentUser!!.uid)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Logowanie nie powiodło się!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}