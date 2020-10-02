
package com.wsr.checklist.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.adapter.ListAdapter
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.view_model.EditViewModel
import kotlinx.android.synthetic.main.activity_show_contents.*
import java.util.*

class ShowContents : AppCompatActivity() {

    private lateinit var viewModel: AppViewModel
    private lateinit var editViewModel: EditViewModel
    private lateinit var title: String
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_contents)

        //MainActivityからの引数を代入
        title = intent.getStringExtra("TITLE")!!

        //インスタンス形成
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        editViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        adapter = ListAdapter(this, title, viewModel, editViewModel)
        val layoutManager = LinearLayoutManager(this)

        //RecyclerViewの設定
        val recyclerView = findViewById<RecyclerView>(R.id.ContentRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        /*
        下の３つのボタンを押した際のそれぞれの処理を記述
         */
        //editボタンを押したとき
        edit_button.setOnClickListener {
            val id = UUID.randomUUID().toString()
            viewModel.insert(InfoList(id , adapter.listForCheck.size, title, false, ""))
            editViewModel.insert(InfoList(id , adapter.listForCheck.size, title, false, ""))
            adapter.notifyDataSetChanged()
        }

        //renameボタンを押したとき
        rename_button.setOnClickListener {
            val editText = EditText(this)
            editText.setText(title)

            //Renameのためのアラートダイアログの表示
            AlertDialog.Builder(this)
                .setTitle("Rename title")
                .setMessage("Input the title")
                .setView(editText)
                .setPositiveButton("OK") { dialog, which ->

                    //新しいチェックリストのタイトルの入った変数
                    title = MainActivity().checkTitle(editText.text.toString(), adapter.titleList)
                    for (i in adapter.list){
                        viewModel.changeTitle(i.id, title)
                    }
                    adapter.title = title
                    show_toolbar.title = title
                }
                .setNegativeButton("Cancel"){dialog, which ->}
                .setCancelable(false)
                .show()
        }

        //check outボタンを押したとき
        check_out_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Waring")
                .setMessage("Do you want to check out all elements?")
                .setPositiveButton("Yes") { dialog, which ->

                    //新しいチェックリストのタイトルの入った変数
                    for (i in adapter.list){
                        viewModel.changeCheck(i.id , false)
                    }
                }
                .setNegativeButton("Cancel"){dialog, which ->}
                .setCancelable(false)
                .show()
        }

        //ToolBarの設定
        show_toolbar.title = title
        show_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

        //Backボタンを押した際の処理
        show_toolbar.setNavigationOnClickListener{
            finish()
        }

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })
    }

    override fun onStop() {
        super.onStop()
        val list = editViewModel.editList
        for(i in list){
            viewModel.changeItem(i.id, i.item)
            viewModel.changeCheck(i.id, i.check)
        }
    }
}