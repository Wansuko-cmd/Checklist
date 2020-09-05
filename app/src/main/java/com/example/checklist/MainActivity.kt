package com.example.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //インスタンス形成
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = ListAdapter(this, viewModel)
        val layoutManager = LinearLayoutManager(this)

        //RecyclerViewの設定
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = layoutManager
        RecyclerView.setHasFixedSize(true)

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })
    }
}