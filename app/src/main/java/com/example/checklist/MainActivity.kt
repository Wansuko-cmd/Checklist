package com.example.checklist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = MainAdapter()
        val layoutManager = LinearLayoutManager(this)

        MainRecyclerView.adapter = adapter
        MainRecyclerView.layoutManager = layoutManager
        MainRecyclerView.setHasFixedSize(true)

        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })

        adapter.clickTitleOnListener = {
            val intent = Intent(this, ShowContents::class.java)
            startActivity(intent)
        }
    }
}
