package com.wsr.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_holder.MainViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList

class MainAdapter():
    RecyclerView.Adapter<MainViewHolder>(){

    //LiveDataの内容を代入
    private var list = emptyList<InfoList>()

    //Titleをクリックされたときに実行される関数名
    var clickTitleOnListener: () -> Unit = {}

    //ViewHolderのインスタンス化
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.show_title, parent, false)
        return MainViewHolder(view)
    }

    //LiveDataの入っている変数の長さを返す関数
    override fun getItemCount(): Int {
        return  list.size
    }

    //インスタンス化したViewHolderの中の値の変更
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.title.setOnClickListener{
            clickTitleOnListener()
        }
    }

    //LiveDataの内容をMainAdapterのインスタンスに反映させる関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        this.list = lists
        notifyDataSetChanged()
    }
}