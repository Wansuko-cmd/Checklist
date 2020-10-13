package com.wsr.shopping_friend.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsr.shopping_friend.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbarの設定
        main_toolbar.inflateMenu(R.menu.menu_for_show)
    }
}