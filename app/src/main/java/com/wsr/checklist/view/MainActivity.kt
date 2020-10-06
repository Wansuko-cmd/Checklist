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
import com.wsr.checklist.type_file.renameAlert
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //インスタンス形成
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = MainAdapter(this, viewModel)
        val layoutManager = LinearLayoutManager(this)

        //Recyclerviewの設定
        MainRecyclerView.adapter = adapter
        MainRecyclerView.layoutManager = layoutManager
        MainRecyclerView.setHasFixedSize(true)

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })

        //MainAdapterにあるclickTitleOnListener関数の設定
        adapter.clickTitleOnListener = {
            val intent = Intent(this, ShowContents::class.java)
            intent.putExtra("TITLE", it)
            startActivity(intent)
        }

        //新しくチェックリストを作成するためのfabの形成
        fab.setOnClickListener{
            renameAlert(this, makeShowContents, adapter.titleList, "")
        }
    }

    private val makeShowContents: (String) -> Unit = { title ->
        val intent = Intent(this, ShowContents::class.java)
        intent.putExtra("TITLE", title)
        startActivity(intent)
    }
}