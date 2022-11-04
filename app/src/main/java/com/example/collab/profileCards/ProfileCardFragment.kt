package com.example.collab.profileCards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.collab.R

class ProfileCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_card, container, false)
    }

    companion object {
        fun newInstance(): ProfileCardFragment {
            return ProfileCardFragment()
        }
    }

}