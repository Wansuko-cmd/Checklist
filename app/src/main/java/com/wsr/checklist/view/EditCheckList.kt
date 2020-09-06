package com.wsr.checklist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsr.checklist.R
import com.wsr.checklist.adapter.EditAdapter
import com.wsr.checklist.adapter.MainAdapter
import com.wsr.checklist.view_model.AppViewModel
import kotlinx.android.synthetic.main.activity_edit_check_list.*

class EditCheckList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_check_list)

        val saveButton = findViewById<Button>(R.id.save_button)
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = EditAdapter()
        val layoutManager = LinearLayoutManager(this)

        content_recyclerView.adapter = adapter
        content_recyclerView.layoutManager = layoutManager
        content_recyclerView.setHasFixedSize(true)

        saveButton.setOnClickListener{

        }
    }
}