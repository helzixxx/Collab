package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
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
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container,
                profileCardFragment,
                "profileCardFragment"
            )
            .commit()

        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        bottomNav.setOnNavigationItemReselectedListener {
//            when (it.itemId) {
//                R.id.chat -> {
//                    if ((::profileCardFragment.isInitialized && profileCardFragment.isVisible) ||
//                        (::userProfileFragment.isInitialized && userProfileFragment.isVisible)
//                    ) {
//                        chatListFragment = ChatListFragment.newInstance()
//                        supportFragmentManager
//                            .beginTransaction()
//                            .replace(
//                                R.id.container,
//                                chatListFragment,
//                                "chatListFragment"
//                            )
//                            .commit()
//                    }
//                }
//                R.id.cards -> {
//                    if ((::chatListFragment.isInitialized && chatListFragment.isVisible) ||
//                        (::userProfileFragment.isInitialized && userProfileFragment.isVisible)
//                    ) {
//                        profileCardFragment = ProfileCardFragment.newInstance()
//                        supportFragmentManager
//                            .beginTransaction()
//                            .replace(
//                                R.id.container,
//                                profileCardFragment,
//                                "profileCardFragment"
//                            )
//                            .commit()
//                    }
//                }
//                R.id.profile -> {
//                    if ((::chatListFragment.isInitialized && chatListFragment.isVisible) ||
//                        (::profileCardFragment.isInitialized && profileCardFragment.isVisible)
//                    ) {
//                        userProfileFragment = UserProfileFragment.newInstance()
//                        supportFragmentManager
//                            .beginTransaction()
//                            .replace(
//                                R.id.container,
//                                userProfileFragment,
//                                "userProfileFragment"
//                            )
//                            .commit()
//                    }
//                }
//           }
//        }
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.chat -> {
                    val fragment = ChatListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.cards -> {
                    val fragment = ProfileCardFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val fragment = UserProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

}