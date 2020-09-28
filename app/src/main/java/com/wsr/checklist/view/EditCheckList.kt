package com.wsr.checklist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        //MainActivity、もしくはShowContentsから渡された、チェックリストのタイトルを変数に代入
        val title = intent.getStringExtra("TITLE")

        //ViewModelのインスタンスを形成
        val viewModel: EditViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        val appViewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        //その他のインスタンスを形成
        val editAdapter = EditAdapter(title!!, viewModel)
        val layoutManager = LinearLayoutManager(this)

        //RecyclerViewの設定
        content_recyclerView.adapter = editAdapter
        content_recyclerView.layoutManager = layoutManager
        content_recyclerView.setHasFixedSize(true)

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.editList.observe(this, Observer{list ->
            list?.let{editAdapter.maintainList(list)}
        })

        appViewModel.infoList.observe(this, Observer{list ->
            list?.let{editAdapter.setInfoList(list)}
        })

        //saveButtonが押された際に実行される関数
        save_button.setOnClickListener{
            appViewModel.deleteWithTitle(title)
            editAdapter.list.sortBy{it.id}
            val list = editAdapter.list.filter { it.item != "" }
            list.sortedBy { it.id }
            for ((count, i) in list.withIndex()){
                appViewModel.insert(InfoList(UUID.randomUUID().toString(), count, title, false, i.item))
            }
            finish()
        }
    }
}