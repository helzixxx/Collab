package com.example.collab.profileCards


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.collab.MainActivity
import com.example.collab.R


class ProfileCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView : View? = inflater.inflate(R.layout.fragment_profile_card, container, false)

        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.findItem(R.id.filter).isVisible = true
        toolbar.menu.findItem(R.id.search).isVisible = false
        toolbar.menu.findItem(R.id.settings).isVisible = false

        val profileCardLayout = rootView!!.findViewById<CardView>(R.id.profileCardLayout)

        //todo card swipe

//        profileCardLayout.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
//
//            view.performClick()
//            return@OnTouchListener true
//        })


        return rootView
    }

    companion object {
        fun newInstance(): ProfileCardFragment {
            return ProfileCardFragment()
        }
    }

}