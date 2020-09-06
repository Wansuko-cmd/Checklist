package com.example.checklist

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListAdapter(context: Context, private val viewModel: AppViewModel):
    RecyclerView.Adapter<ListViewHolder>(){

    //Activityを変数に代入
    private val context: Context = context
    //LiveDataから得られた値を収納する変数
    private var list = emptyList<InfoList>()

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        return ListViewHolder(view)
    }

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
        this.list = lists
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