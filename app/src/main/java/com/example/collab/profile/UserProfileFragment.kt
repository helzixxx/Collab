package com.example.collab.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R
import com.example.collab.SettingsActivity

class UserProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.settings).isVisible = true
        toolbar.menu.findItem(R.id.search).isVisible = false
        toolbar.menu.findItem(R.id.filter).isVisible = false

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    val intent = Intent(requireActivity() as MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> { false }
            }
        }

        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    companion object {
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}