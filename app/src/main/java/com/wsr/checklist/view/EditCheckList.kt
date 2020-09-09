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
        appViewModel.infoList.observe(this, Observer{list ->
            list?.let{editAdapter.setInfoList(list)}
        })

        //saveButtonが押された際に実行される関数
        save_button.setOnClickListener{
            appViewModel.deleteWithTitle(title)
            viewModel.editList.sortBy{it.id}
            for (i in viewModel.editList){
                if (i.item != "") appViewModel.insert(InfoList(UUID.randomUUID().toString(), title, false, i.item))
            }
            finish()
        }
    }
}