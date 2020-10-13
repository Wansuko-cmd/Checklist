package com.wsr.shopping_friend.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wsr.shopping_friend.R

class ShowPreference :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_preference)

        when(intent.getStringExtra("Purpose")){
            "settings" -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings_container, PreferenceFragment())
                    .commit()
            }
            else -> finish()
        }
    }
}