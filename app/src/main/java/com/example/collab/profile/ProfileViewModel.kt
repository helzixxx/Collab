package com.example.collab.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collab.models.Genre
import com.example.collab.models.Instrument

class ProfileViewModel : ViewModel() {

    val genres = MutableLiveData<ArrayList<Genre?>?>()
    val instruments = MutableLiveData<ArrayList<Instrument?>?>()

//    val genresFilter = MutableLiveData<ArrayList<Genre?>?>()
//    val instrumentsFilter  = MutableLiveData<ArrayList<Instrument?>?>()

}