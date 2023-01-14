package com.example.collab.config

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class ConfigData {
    val PREF_USER_NAME = "username"

    fun getSharedPreferences(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setUserName(context: Context?, userName: String?) {
        val editor= getSharedPreferences(context).edit()
        editor.putString(PREF_USER_NAME, userName)
        editor.apply()
        editor.commit()
    }

    fun getUserName(context: Context?): String? {
        return getSharedPreferences(context).getString(PREF_USER_NAME, "")
    }
}