package com.wsr.checklist.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.adapter.MainAdapter
import com.wsr.checklist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //インスタンス形成
        val viewModel: AppViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        val adapter = MainAdapter(this, viewModel)
        val layoutManager = LinearLayoutManager(this)

        //Recyclerviewの設定
        MainRecyclerView.adapter = adapter
        MainRecyclerView.layoutManager = layoutManager
        MainRecyclerView.setHasFixedSize(true)

        //LiveDataの監視、値が変更した際に実行する関数の設定
        viewModel.infoList.observe(this, Observer{list ->
            list?.let{adapter.setInfoList(it)}
        })

        //MainAdapterにあるclickTitleOnListener関数の設定
        adapter.clickTitleOnListener = {
            val intent = Intent(this, ShowContents::class.java)
            intent.putExtra("TITLE", it)
            startActivity(intent)
        }

        //新しくチェックリストを作成するためのfabの形成
        fab.setOnClickListener{
            //タイトルを入力するアラートダイアログを出力
            val editText = EditText(this)
                AlertDialog.Builder(this)
                    .setTitle("Title")
                    .setMessage("Input the title")
                    .setView(editText)
                    .setPositiveButton("OK") { dialog, which ->
                        //新しく作成するチェックリストのタイトルの入った変数
                        val title = checkTitle(editText.text.toString(), adapter.titleList)

                        //新しく作成するチェックリストの中身を記入するためのインテント
                        /*val intent = Intent(this, EditCheckList::class.java)
                        intent.putExtra("TITLE", title)
                        startActivity(intent)*/
                    }
                    .setNegativeButton("Cancel"){dialog, which ->}
                    .setCancelable(false)
                    .show()
        }
    }

    //同じ名前のタイトルがないかを確認する関数
    fun checkTitle(title: String, titleList: List<String>): String{
        var title = title
        if (title == "") title = "Non-Title"
        for (i in titleList) {
            if (title == i) title = addNumber(title, titleList)
        }
        return title
    }

    //タイトルの後ろに、同じ名前にならないように数字をつける関数
    private fun addNumber(title: String, titleList: List<String>) : String{
        var tempTitle: String
        var num = 1
        while(true) {
            var result = true
            tempTitle = "$title($num)"
            for (i in titleList) {
                if (tempTitle == i) result = false
            }
            if (result) break
            num++
        }
        return tempTitle
    }
}