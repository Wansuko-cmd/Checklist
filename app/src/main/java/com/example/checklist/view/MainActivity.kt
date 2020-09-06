package com.example.checklist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checklist.view_model.AppViewModel
import com.example.checklist.adapter.MainAdapter
import com.example.checklist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //インスタンス化
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = MainAdapter()
        val layoutManager = LinearLayoutManager(this)

        //Recyclerviewの設定
        MainRecyclerView.adapter = adapter
        MainRecyclerView.layoutManager = layoutManager
        MainRecyclerView.setHasFixedSize(true)

        //監視するLiveDataの設定
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })

        //MainAdapterにあるclickTitleOnListener関数の設定
        adapter.clickTitleOnListener = {
            val intent = Intent(this, ShowContents::class.java)
            startActivity(intent)
        }
    }
}
