package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.collab.adapters.ChatListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.collab.chat.ChatListFragment
import com.example.collab.profile.UserProfileFragment
import com.example.collab.cards.CardsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private lateinit var cardsFragment: CardsFragment
    private lateinit var userProfileFragment: UserProfileFragment
    private lateinit var chatListFragment: ChatListFragment




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val userId = intent.getStringExtra("UserId")
        var intentCheck = intent.getStringExtra("Check")




        cardsFragment = CardsFragment.newInstance()
        userProfileFragment = UserProfileFragment.newInstance()
        chatListFragment = ChatListFragment.newInstance()

       // val toolbar = findViewById<Toolbar>(R.id.toolbar)


        when(intentCheck) {
            "1"-> {
                //intentCheck = ""
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, userProfileFragment, "userProfileFragment").commit()
            }
            "2" -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, chatListFragment, "chatListFragment").commit()
            }
            else -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, cardsFragment, "profileCardFragment").commit()
            }
        }
//        if (intentCheck == "1") {
//            intentCheck = ""
//            supportFragmentManager.beginTransaction()
//                .add(R.id.container, userProfileFragment, "userProfileFragment").commit()
//        } else {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.container, cardsFragment, "profileCardFragment").commit()
//        }

        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener {
            val profileCardFragmentVisibility =
                ::cardsFragment.isInitialized && cardsFragment.isVisible
            val userProfileFragmentVisibility =
                ::userProfileFragment.isInitialized && userProfileFragment.isVisible
            val chatListFragmentVisibility =
                ::chatListFragment.isInitialized && chatListFragment.isVisible

            when (it.itemId) {
                R.id.chat -> {
                    if (!chatListFragmentVisibility) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, chatListFragment, "chatListFragment").commit()
                    }
                    true
                }
                R.id.cards -> {
                    if (!profileCardFragmentVisibility) {
                        //if (isDown) hide()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, cardsFragment, "profileCardFragment").commit()
                    }
                    true
                }
                R.id.profile -> {
                    //if (isDown) hide()
                    if (!userProfileFragmentVisibility) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, userProfileFragment, "userProfileFragment")
                            .commit()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }





    }




}