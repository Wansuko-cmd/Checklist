package com.wsr.checklist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wsr.checklist.R
import com.wsr.checklist.preference.PreferenceFragment

class ShowPreference :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_preference)

        //設定画面のフラグメントの設定
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, PreferenceFragment())
            .commit()
    }
}