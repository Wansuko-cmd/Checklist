package com.wsr.checklist.adapter

import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_holder.ListViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.type_file.CustomTextWatcher
import com.wsr.checklist.view_model.EditViewModel

class ListAdapter(
    private val editViewModel: EditViewModel):
    RecyclerView.Adapter<ListViewHolder>() {

    //使用する関数の定義
    var changeText: (p0: String, position: Int) -> Unit = {_,_  ->}
    var changeCheck: (check: Boolean, position: Int) -> Unit = {_, _ ->}
    var deleteElement: (position: Int) -> Unit = {}
    var focus = -1

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        return ListViewHolder(view)
    }

    //LiveDataの入っている変数の長さを返す関数
    override fun getItemCount(): Int {
        return editViewModel.getList().size
    }

    //ViewHolderのインスタンスの保持する値を変更
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //データベースの情報を格納するためのプロセス
        for (i in editViewModel.getList()) {
            if (holder.adapterPosition == i.number) {
                holder.check.isChecked = editViewModel.getCheck(i.id)
                holder.item.setText(editViewModel.getItem(i.id))
                break
            }
        }

        //focusを当てる処理
        if(holder.adapterPosition == focus){
            holder.item.requestFocus()
        }

        //チェックのついてないところを色付けするためのプロセス
        if (holder.check.isChecked) {
            holder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
        }

        //アイテムが変更されたときにeditViewModelの保持する値を変更するためのプロセス
        holder.item.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                changeText(p0.toString(), holder.adapterPosition)
            }
        })

        //チェックの状態が変更したときにデータベースに保存するためのプロセス
        holder.check.setOnClickListener {
            changeCheck(holder.check.isChecked, holder.adapterPosition)
        }
    }
}