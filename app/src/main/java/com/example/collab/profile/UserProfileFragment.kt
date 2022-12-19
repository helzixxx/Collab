package com.example.collab.profile

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R

class UserProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.settings).isVisible = true
        toolbar.menu.findItem(R.id.search).isVisible = false
        toolbar.menu.findItem(R.id.filter).isVisible = false

        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    companion object {
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}