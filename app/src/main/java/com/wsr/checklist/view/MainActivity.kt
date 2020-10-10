package com.wsr.checklist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsr.checklist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbarの設定
        main_toolbar.inflateMenu(R.menu.menu_for_show)
        main_toolbar.menu.setGroupVisible(R.id.rename_group, false)
        main_toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> showSettings()
            }
            true
        }
    }

    //設定画面に画面遷移するための処理
    private fun showSettings(){
        val intent = Intent(this, ShowPreference::class.java)
        startActivity(intent)
    }
}