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
import com.example.collab.config.ConfigData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var context: Context

    private lateinit var userName: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordTextOne: EditText
    private lateinit var passwordTextTwo: EditText

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
        passwordTextOne = findViewById(R.id.passwordTextOne)
        passwordTextTwo = findViewById(R.id.passwordTextTwo)

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
        val password = passwordTextOne.text.toString()
        val passwordRepeat = passwordTextTwo.text.toString()

        // check pass
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email i Hasło nie mogą być puste", Toast.LENGTH_SHORT).show()
            return
        } else if( password != passwordRepeat ) {
            Toast.makeText(this, "Podane hasła są różne", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isComplete && task.isSuccessful) {
                Toast.makeText(this, "Jesteś zarejestrowany", Toast.LENGTH_SHORT).show()
                val userId = auth.currentUser!!.uid
                var databaseReference: DatabaseReference =
                    FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                var newUser: HashMap<String, String> = HashMap()
                newUser["name"] = name

                databaseReference.updateChildren(newUser as Map<String, Any>)

                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Rejestracja nie powiodła się!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}