package com.wsr.checklist.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.view_holder.ListViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList

class ListAdapter(context: Context, private val title: String,  private val viewModel: AppViewModel):
    RecyclerView.Adapter<ListViewHolder>(){

    //Activityを変数に代入
    private val context: Context = context
    //LiveDataから得られた値を収納する変数
    var list = emptyList<InfoList>()

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
        holder.check.isChecked = list[position].check
        holder.item.text = list[position].item
        holder.check.setOnClickListener{
            /*if (!holder.check.isChecked) {
                makeSureCheckOut(holder, position)
            }*/
            viewModel.changeCheck(list[position].id, holder.check.isChecked)
        }
    }

    //LiveDataの値が変更した際に実行される関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        val tempList = mutableListOf<InfoList>()
        for (numOfTitle in lists){
            if (numOfTitle.title == title){
                tempList.add(numOfTitle)
            }
        }
        list = tempList
        notifyDataSetChanged()
    }

    //チェックを外す際に確認をとる関数
    private fun makeSureCheckOut(holder: ListViewHolder, position: Int){
        AlertDialog.Builder(context)
            .setTitle(list[position].item)
            .setMessage("Do you really want to check out it?")
            .setPositiveButton("Yes") {dialog, which ->
                holder.check.isChecked = false
            }
            .setNegativeButton("No") { dialog, which ->
                holder.check.isChecked = true
            }
            .setCancelable(false)
            .show()
    }
}