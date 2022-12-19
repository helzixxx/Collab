package com.example.collab.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R


class ChatListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.search).isVisible = true
        toolbar.menu.findItem(R.id.filter).isVisible = false
        toolbar.menu.findItem(R.id.settings).isVisible = false
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    companion object {
        fun newInstance(): ChatListFragment {
            return ChatListFragment()
        }
    }

}