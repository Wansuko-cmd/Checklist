package com.wsr.checklist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.adapter.MainAdapter
import com.wsr.checklist.R
import com.wsr.checklist.fragments.ShowTitleFragment
import com.wsr.checklist.type_file.renameAlert
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, ShowTitleFragment())
            .commit()

        main_toolbar.inflateMenu(R.menu.menu_for_show)

        main_toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> showSettings()
            }
            true
        }
    }

    private fun showSettings(){
        val intent = Intent(this, ShowPreference::class.java)
        startActivity(intent)
    }
}