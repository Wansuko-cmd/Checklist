package com.wsr.checklist.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.CustomTextWatcher
import com.wsr.checklist.type_file.EditList
import com.wsr.checklist.view_holder.EditViewHolder
import com.wsr.checklist.view_model.EditViewModel

class EditAdapter(private val title: String, private val viewModel: EditViewModel):
    RecyclerView.Adapter<EditViewHolder>() {

    //編集するチェックリストの中身をidと共に保存するためのリスト
    var list = mutableListOf<EditList>()

    //ViewHolderのインスタンス化
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.edit_contents, parent, false)
        return EditViewHolder(view)
    }

    //listのサイズを返す関数。変数positionに影響
    override fun getItemCount(): Int {
        return list.size + 1
    }

    //インスタンス化したViewHolderの中の値の変更
    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        //データベースの情報を格納するためのプロセス
        if (list.size > position) {
            list.sortBy { it.id }
            holder.edit.setText(list[position].item)
        } else {
            holder.edit.text = null
        }

        //編集内容をこのクラスのインスタンスに代入するための関数
        holder.edit.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var exist = false
                for (i in list) {
                    if (holder.adapterPosition == i.id) {
                        viewModel.changeItem(holder.adapterPosition, holder.edit.text.toString())
                        exist = true
                        break
                    }
                }
                if (!exist) viewModel.insert(
                    EditList(
                        holder.adapterPosition,
                        holder.edit.text.toString()
                    )
                )
            }
        })

        holder.delete.setOnClickListener {
            if (holder.adapterPosition < list.size){
                list.removeAt(holder.adapterPosition)
                for ((count, i) in list.withIndex()){
                    i.id = count
                }
                viewModel.update(list)
            }
        }
    }

    //EditViewModelのLiveDataの内容をEditAdapterのインスタンスのlistに反映させる関数
    internal fun maintainList(lists: MutableList<EditList>){
        this.list = lists
        list.sortBy { it.id }
        notifyDataSetChanged()
    }

    //最初にデータベースの内容をEditViewModelに格納するための関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        if(list.size == 0){
            for (i in lists) {
                if (i.title == title) {
                    viewModel.insert(EditList(i.number, i.item))
                }
            }
        }
        list.sortBy { it.id }
        notifyDataSetChanged()
    }
}