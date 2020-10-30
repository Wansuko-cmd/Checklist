package com.wsr.shopping_friend.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wsr.shopping_friend.R

//設定を出すためのActivity
class ShowPreference :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_preference)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, PreferenceFragment())
            .commit()
    }
}