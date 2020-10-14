package com.wsr.shopping_friend.preference

import android.content.Context
import androidx.preference.PreferenceManager
import com.wsr.shopping_friend.R

fun checkVersion(context: Context){
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    if(pref.getString("version", "1.0") != context.getString(R.string.setting_version)){
        pref.edit()
            .clear()
            .apply()
    }
}

fun getTextSize(context: Context): Int{
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return when(pref.getString("text_size", "medium")){
        "small" -> 25
        "medium" -> 30
        "large" -> 35
        else -> 30
    }
}

fun getDefaultTitle(context: Context): String{
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return when(pref.getString("default_title", "Non-title")){
        null -> "Non-title"
        "" -> "Empty-title"
        else -> pref.getString("default_title", "Non-title")!!
    }
}