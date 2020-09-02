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


        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = ListAdapter(viewModel)
        val layoutManager = LinearLayoutManager(this)
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = layoutManager
        RecyclerView.setHasFixedSize(true)
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })
    }
}