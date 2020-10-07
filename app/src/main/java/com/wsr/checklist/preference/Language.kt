package com.wsr.checklist.preference

import android.content.SharedPreferences
import android.content.res.Resources
import android.content.res.loader.ResourcesLoader
import com.wsr.checklist.R
import org.intellij.lang.annotations.Identifier
import javax.microedition.khronos.egl.EGL

fun changeLanguage(pref: SharedPreferences, name: String){
    var lan: String = ""
    when(pref.getString("language", "")){
        "English" -> lan = name + "Eg"
        "Japanese" -> lan = name + "Jp"
    }


}