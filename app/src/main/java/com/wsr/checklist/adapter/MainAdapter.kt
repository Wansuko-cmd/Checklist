package com.wsr.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_holder.MainViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList

class MainAdapter():
    RecyclerView.Adapter<MainViewHolder>(){

    var titleList = mutableListOf<String>()

    //Titleをクリックされたときに実行される関数名
    var clickTitleOnListener: (title: String) -> Unit = {}
    var clickDeleteOnListener: (title: String, position: Int) -> Unit = {_, _ ->}

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

        holder.title.setOnClickListener {
            clickTitleOnListener(holder.title.text.toString())
        }

        holder.delete.setOnClickListener {
            clickDeleteOnListener(titleList[holder.adapterPosition], holder.adapterPosition)
        }
    }

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