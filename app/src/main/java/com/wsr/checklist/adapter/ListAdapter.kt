package com.wsr.checklist.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.view_holder.ListViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.CustomTextWatcher
import com.wsr.checklist.type_file.EditList
import com.wsr.checklist.view_model.EditViewModel

class ListAdapter(
    private val context: Context,
    var title: String,
    private val viewModel: AppViewModel,
    private val editViewModel: EditViewModel):
    RecyclerView.Adapter<ListViewHolder>(){

    //チェックリストのタイトルを全て格納する変数
    var titleList = mutableListOf<String>()

    //選択されたタイトルのチェックリストの全ての情報を格納する変数
    var list = mutableListOf<InfoList>()

    var listForCheck = emptyList<InfoList>()

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        return ListViewHolder(view)
    }

    //LiveDataの入っている変数の長さを返す関数
    override fun getItemCount(): Int {
        return  list.size
    }

    //ViewHolderのインスタンスの保持する値を変更
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //データベースの情報を格納するためのプロセス
        for (i in listForCheck) {
            if (holder.adapterPosition == i.number) {
                holder.check.isChecked = i.check
                holder.item.setText(i.item)
            }
        }

        //チェックのついてないところを色付けするためのプロセス
        if (holder.check.isChecked) {
            holder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
        }

        holder.item.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                for (i in list){
                    if(holder.adapterPosition == i.number){
                        //list[holder.adapterPosition] = list[holder.adapterPosition].copy(item = p0.toString())
                        editViewModel.changeItem(i.number, p0.toString())
                        break
                    }
                }
            }
        })

        //チェックの状態が変更したときにデータベースに保存するためのプロセス
        holder.check.setOnClickListener {
            for (i in list) {
                if (holder.adapterPosition == i.number) {
                    //list[holder.adapterPosition] = list[holder.adapterPosition].copy(check = holder.check.isChecked)
                    //editViewModel.changeCheck(i.number, holder.check.isChecked)
                    viewModel.changeCheck(i.id, holder.check.isChecked)
                    notifyDataSetChanged()
                    break
                }
            }
        }
    }

    //LiveDataの値が変更した際に実行される関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        for (numOfTitle in lists){
            if (!titleList.contains(numOfTitle.title)){
                titleList.add(numOfTitle.title)
            }
        }
        val tempList = mutableListOf<InfoList>()
        for (numOfTitle in lists){
            if (numOfTitle.title == title){
                tempList.add(numOfTitle)
                editViewModel.insert(numOfTitle)
            }
        }
        tempList.sortBy { it.number }
        list = tempList
        listForCheck = sortTrueFalse(list)
        notifyDataSetChanged()
    }

    internal fun maintainList(lists: MutableList<InfoList>){
        this.list = lists
        list.sortBy { it.number }
        notifyDataSetChanged()
    }

    private fun sortTrueFalse(list: List<InfoList>): List<InfoList>{
        list.sortedBy { it.number }
        val onlyFalseList: List<InfoList> = list.filter{ !it.check}
        val onlyTrueList: List<InfoList> = list.filter{ it.check }
        val result = onlyFalseList + onlyTrueList
        for ((num, i) in result.withIndex()){
            i.number = num
        }
        return result
    }
}