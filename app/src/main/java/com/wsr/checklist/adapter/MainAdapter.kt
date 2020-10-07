package com.wsr.checklist.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_holder.MainViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.view_model.AppViewModel

class MainAdapter(
    private val context: Context,
    private val viewModel: AppViewModel):
    RecyclerView.Adapter<MainViewHolder>(){

    //LiveDataのから抽出したタイトルのみのリスト代入
    var titleList: MutableList<String> = mutableListOf()

    //Titleをクリックされたときに実行される関数名
    var clickTitleOnListener: (title: String) -> Unit = {}

    //ViewHolderのインスタンス化
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.show_title, parent, false)
        return MainViewHolder(view)
    }

    //LiveDataの入っている変数の長さを返す関数
    override fun getItemCount(): Int {
        return  titleList.size
    }

    //インスタンス化したViewHolderの中の値の変更
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.title.text = titleList[position]
        holder.title.setOnClickListener{
            clickTitleOnListener(holder.title.text.toString())
        }
        holder.delete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.delete_with_title_title)
                .setMessage(R.string.delete_with_title_message)
                .setPositiveButton(R.string.delete_with_title_positive) { _, _ ->
                    viewModel.deleteWithTitle(titleList[holder.adapterPosition])
                }
                .setNegativeButton(R.string.delete_with_title_negative, null)
                .setCancelable(true)
                .show()
        }
    }

    //LiveDataの内容をMainAdapterのインスタンスに反映させる関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        titleList = mutableListOf()
        for (numOfTitle in lists){
            if (!titleList.contains(numOfTitle.title)){
                titleList.add(numOfTitle.title)
            }
        }
        titleList.sort()
        notifyDataSetChanged()
    }
}