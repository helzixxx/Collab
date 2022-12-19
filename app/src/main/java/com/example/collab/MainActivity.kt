package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.collab.chat.ChatListFragment
import com.example.collab.profile.UserProfileFragment
import com.example.collab.profileCards.ProfileCardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView

    private lateinit var profileCardFragment: ProfileCardFragment
    private lateinit var userProfileFragment: UserProfileFragment
    private lateinit var chatListFragment: ChatListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        profileCardFragment = ProfileCardFragment.newInstance()
        userProfileFragment = UserProfileFragment.newInstance()
        chatListFragment = ChatListFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, profileCardFragment, "profileCardFragment").commit()

        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener {
            val profileCardFragmentVisibility = ::profileCardFragment.isInitialized && profileCardFragment.isVisible
            val userProfileFragmentVisibility = ::userProfileFragment.isInitialized && userProfileFragment.isVisible
            val chatListFragmentVisibility = ::chatListFragment.isInitialized && chatListFragment.isVisible

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
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, profileCardFragment, "profileCardFragment").commit()
                    }
                    true
                }
                R.id.profile -> {
                    if (!userProfileFragmentVisibility) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, userProfileFragment, "userProfileFragment").commit()
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

//        if(::profileCardFragment.isInitialized && profileCardFragment.isVisible){
//            toolbar.menu.findItem(R.id.settings).isVisible = true
//        } else if(::userProfileFragment.isInitialized && userProfileFragment.isVisible){
//            toolbar.menu.findItem(R.id.filter).isVisible = true
//        } else if(::chatListFragment.isInitialized && chatListFragment.isVisible){
//            toolbar.menu.findItem(R.id.search).isVisible = true
//        }

//        toolbar.setOnMenuItemClickListener{ item ->
//            when (item.itemId) {
//                R.id.filter -> {
//                    //processPlannedPzDocumentFragment.showDialogBarcode()
//                }
//                R.id.settings -> {
//
//                }
//                R.id.search -> {
//
//                }
//            }
//            false
//        }

    }


}