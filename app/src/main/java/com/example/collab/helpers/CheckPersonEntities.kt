package com.example.collab.helpers

import com.example.collab.models.Person

class CheckPersonEntities {

    fun checkPerson(person : Person?) : Person? {
        if (person!!.name.isNullOrBlank()){
            person.name = ""
        }
        if (person.bio.isNullOrBlank()){
            person.bio = ""
        }
        if (person.surname.isNullOrBlank()){
            person.surname = ""
        }
        if (person.profession.isNullOrBlank()){
            person.profession = ""
        }
        if (person.township.isNullOrBlank()){
            person.township = ""
        }

        return person
    }
}