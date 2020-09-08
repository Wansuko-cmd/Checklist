package com.wsr.checklist.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsr.checklist.R
import com.wsr.checklist.adapter.EditAdapter
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.view_model.EditViewModel
import kotlinx.android.synthetic.main.activity_edit_check_list.*
import java.util.*

class EditCheckList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_check_list)

        val title = intent.getStringExtra("TITLE")
        val saveButton = findViewById<Button>(R.id.save_button)

        val viewModel: EditViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        val appViewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        val editAdapter = EditAdapter(title!!, viewModel)

        val layoutManager = LinearLayoutManager(this)

        content_recyclerView.adapter = editAdapter
        content_recyclerView.layoutManager = layoutManager
        content_recyclerView.setHasFixedSize(true)


        appViewModel.infoList.observe(this, Observer{list ->
            list?.let{editAdapter.setInfoList(list)}
        })


        saveButton.setOnClickListener{
            appViewModel.deleteWithTitle(title)
            for (i in viewModel.editList){
                if (i.item != "") appViewModel.insert(InfoList(UUID.randomUUID().toString(), title!!, false, i.item))
            }
            finish()
        }
    }
}