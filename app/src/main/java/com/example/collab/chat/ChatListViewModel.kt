package com.example.collab.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collab.models.Genre
import com.example.collab.models.Match

class ChatListViewModel : ViewModel() {

    val chats = MutableLiveData<ArrayList<Match>>()
}