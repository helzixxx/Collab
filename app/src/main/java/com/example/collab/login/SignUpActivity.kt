package com.example.collab.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.collab.MainActivity
import com.example.collab.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var context: Context

    private lateinit var userName: EditText
    private lateinit var emailText: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var passwordText: EditText

    private lateinit var auth: FirebaseAuth
    //private lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener
    //private var emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        context = this

        userName = findViewById(R.id.userName)
        emailText = findViewById(R.id.email)
        phoneNumber = findViewById(R.id.phoneNumber)
        passwordText = findViewById(R.id.passwordText)

        val redirectLogin: TextView = findViewById(R.id.redirectLogin)
        val signupButton: Button = findViewById(R.id.signupBtn)

        signupButton.setOnClickListener {
            signUp()
        }

        redirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp() {
        val name = userName.text.toString()
        val email = emailText.text.toString()
        val phone = phoneNumber.text.toString()
        val password = passwordText.text.toString()

        // check pass
        if (email.isBlank() || password.isBlank() ) {
            Toast.makeText(this, "Email i Hasło nie mogą być puste", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Jesteś zarejestrowany", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Rejestracja nie powiodła się!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}