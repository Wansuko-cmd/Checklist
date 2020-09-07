package com.wsr.checklist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsr.checklist.R
import com.wsr.checklist.adapter.EditAdapter
import com.wsr.checklist.view_model.EditViewModel
import kotlinx.android.synthetic.main.activity_edit_check_list.*

class EditCheckList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_check_list)

        val saveButton = findViewById<Button>(R.id.save_button)
        val viewModel: EditViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        val editAdapter = EditAdapter(viewModel)
        val LayoutManager = LinearLayoutManager(this)

        content_recyclerView.adapter = editAdapter
        content_recyclerView.layoutManager = LayoutManager
        content_recyclerView.setHasFixedSize(true)


        saveButton.setOnClickListener{

        }
    }
}