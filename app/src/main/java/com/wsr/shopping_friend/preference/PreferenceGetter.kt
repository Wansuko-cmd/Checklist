package com.wsr.shopping_friend.preference

import android.content.Context
import androidx.preference.PreferenceManager
import com.wsr.shopping_friend.BuildConfig
import com.wsr.shopping_friend.R

//設定のバージョンを確認する関数
fun checkVersion(context: Context){
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    if(pref.getString("preference_version", "1.0") != context.getString(R.string.setting_version)){
        pref.edit()
            .clear()
            .apply()
    }
    pref.edit().apply {
        putString("app_version", BuildConfig.VERSION_NAME)
        apply()
    }
}

//設定された文字のサイズを確認するための関数
fun getTextSize(context: Context): Int{
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return when(pref.getString("text_size", "medium")){
        "small" -> 25
        "medium" -> 30
        "large" -> 35
        else -> 30
    }
}

fun getToolbarTextTheme(context: Context): String {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getString("toolbar_text_theme", "black") ?: "black"
}

//デフォルトタイトルを確認するための関数
fun getDefaultTitle(context: Context): String{
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return when(pref.getString("default_title", "Non-title")){
        null -> "Non-title"
        "" -> "Empty-title"
        else -> pref.getString("default_title", "Non-title")!!
    }
}

//共有の設定内容を確認するための関数
fun getShareAll(context: Context): Boolean{
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getBoolean("share_all", true)
}