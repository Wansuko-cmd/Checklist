package com.wsr.shopping_friend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.view_holder.MainViewHolder
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.preference.getTextSize

class MainAdapter(private val context: Context):
    RecyclerView.Adapter<MainViewHolder>(){

    //全てのタイトル名を保存するリスト
    var titleList = mutableListOf<String>()

    //使用する関数の定義
    var clickTitleOnListener: (title: String) -> Unit = {}
    var clickDeleteOnListener: (title: String, position: Int) -> Unit = {_, _ ->}

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.show_title, parent, false)
        return MainViewHolder(view)
    }

    //titleListの長さを返す関数
    override fun getItemCount(): Int {
        return  titleList.size
    }

    //インスタンス化したViewHolderの中の値の変更
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.title.text = titleList[position]
        holder.title.textSize = getTextSize(context).toFloat()

        //タイトル名をクリックした際の処理
        holder.title.setOnClickListener {
            clickTitleOnListener(holder.title.text.toString())
        }

        //deleteボタンを押した際の処理
        holder.delete.setOnClickListener {
            clickDeleteOnListener(titleList[holder.adapterPosition], holder.adapterPosition)
        }
    }

    //LiveDataの内容をMainAdapterのインスタンスに反映させる関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        titleList = mutableListOf()
        for (numOfTitle in lists){
            if (!titleList.contains(numOfTitle.title) && numOfTitle.title != ""){
                titleList.add(numOfTitle.title)
            }
        }
        titleList.sort()
        notifyDataSetChanged()
    }
}