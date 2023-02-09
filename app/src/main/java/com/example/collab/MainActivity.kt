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

    private var listView: View? = null
    private var containerSearchView: View? = null
    private var isDown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val userId = intent.getStringExtra("UserId")
        var intentCheck = intent.getStringExtra("Check")

        listView = findViewById(R.id.container)
        containerSearchView = findViewById(R.id.containerSearch)
        containerSearchView?.visibility = View.GONE
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.search -> {
                    if (isDown) hide()
                    else show()
                    true
                }
                else -> {
                    false
                }
            }
        }

        cardsFragment = CardsFragment.newInstance()
        userProfileFragment = UserProfileFragment.newInstance()
        chatListFragment = ChatListFragment.newInstance()

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
                        if (isDown) hide()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, cardsFragment, "profileCardFragment").commit()
                    }
                    true
                }
                R.id.profile -> {
                    if (isDown) hide()
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


        val editTextSearch = findViewById<EditText>(R.id.editTextName)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0 != null && p0 != ""){
                    chatListFragment.chatListAdapter.updateListSearch(p0)
                } else {
                    chatListFragment.chatListAdapter.updateListEntries()
                }

            }

            override fun afterTextChanged(p0: Editable?) {}

        })


    }

    private fun hide() {
        containerSearchView!!.visibility = View.GONE
        val params = ConstraintLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 112, 0, 0)
        listView!!.layoutParams = params
        isDown = false
    }

    private fun show(){
        containerSearchView!!.visibility = View.VISIBLE
        val params = ConstraintLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(0, 224, 0, 0)
        listView!!.layoutParams = params
        isDown = true
    }


}