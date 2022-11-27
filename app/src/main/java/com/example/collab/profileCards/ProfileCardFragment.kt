package com.example.collab.profileCards

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.collab.R
import kotlinx.coroutines.Dispatchers
import java.lang.Float.min

class ProfileCardFragment : Fragment() {

    //@SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView : View? = inflater.inflate(R.layout.fragment_profile_card, container, false)

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