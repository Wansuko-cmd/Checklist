
package com.wsr.checklist.view

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.adapter.ListAdapter
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.renameAlert
import com.wsr.checklist.view_model.EditViewModel
import kotlinx.android.synthetic.main.activity_show_contents.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ShowContents : AppCompatActivity() {

    //それぞれの変数を定義
    private lateinit var viewModel: AppViewModel
    private lateinit var editViewModel: EditViewModel
    private lateinit var title: String
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_contents)

        //MainActivityからの引数を代入
        title = intent.getStringExtra("TITLE")!!

        //インスタンスを代入
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        editViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        adapter = ListAdapter(this, title, viewModel, editViewModel)
        val layoutManager = LinearLayoutManager(this)

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.infoList.observe(this, Observer { list ->
            list?.let { adapter.setInfoList(it) }
        })

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
            adapter.addElements()
        }

        //renameボタンを押したとき
        rename_button.setOnClickListener {
            renameAlert(this, changeTitle, adapter.titleList, title)
        }

        //check outボタンを押したとき
        check_out_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Waring")
                .setMessage("Do you want to check out all elements?")
                .setPositiveButton("Yes") { dialog, which ->

                    //新しいチェックリストのタイトルの入った変数
                    for (i in editViewModel.getList()) {
                        viewModel.changeCheck(i.id, false)
                    }
                }
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show()
        }

        //ToolBarの設定
        show_toolbar.title = title
        show_toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

        //Backボタンを押した際の処理
        show_toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private val changeTitle: (String) -> Unit = { title ->
        for (i in editViewModel.getList()) {
            viewModel.changeTitle(i.id, title)
        }
        adapter.title = title
        show_toolbar.title = title
    }

    //ShowContentsが止められた時に実行される処理
    override fun onStop() {
        super.onStop()
        runBlocking {
            val job = GlobalScope.launch {
                viewModel.deleteWithTitle(title)
            }
            job.join()
        }
        //val list = editViewModel.getList()

        /*for (i in list){
            if(i.item == ""){
                runBlocking {
                    val job = GlobalScope.launch {
                        editViewModel.delete(i.id)
                    }
                    job.join()
                }
            }
        }*/
        val numList = editViewModel.getNumList()

        for (i in numList) {
            runBlocking {
                val job = GlobalScope.launch {
                    viewModel.insert(InfoList(i.id, i.number, title, editViewModel.getCheck(i.id), editViewModel.getItem(i.id)))
                }
                job.join()
            }
        }
    }
}