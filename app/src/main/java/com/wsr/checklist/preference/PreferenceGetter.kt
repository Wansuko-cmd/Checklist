package com.wsr.checklist.preference

import android.content.Context
import androidx.preference.PreferenceManager

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