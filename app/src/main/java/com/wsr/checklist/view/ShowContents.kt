
package com.wsr.checklist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.adapter.ListAdapter
import com.wsr.checklist.R
import kotlinx.android.synthetic.main.activity_show_contents.*

class ShowContents : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_contents)

        //MainActivityからの引数を代入
        val title = intent.getStringExtra("TITLE")

        show_toolbar.title = title
        show_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        show_toolbar.setNavigationOnClickListener{
            finish()
        }
        show_toolbar.inflateMenu(R.menu.menu_for_show)
        show_toolbar.setOnMenuItemClickListener{menuItem ->
            if(menuItem.itemId == R.id.edit){
                val intent = Intent(this, EditCheckList::class.java)
                intent.putExtra("TITLE", title)
                startActivity(intent)
            }
            true
        }

        //インスタンス形成
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = ListAdapter(this, title!!, viewModel)
        val layoutManager = LinearLayoutManager(this)

        //RecyclerViewの設定
        val recyclerView = findViewById<RecyclerView>(R.id.ContentRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })
    }

}