package com.example.checklist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checklist.view_model.AppViewModel
import com.example.checklist.adapter.ListAdapter
import com.example.checklist.R

class ShowContents : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_contents)

        //インスタンス形成
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = ListAdapter(this, viewModel)
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