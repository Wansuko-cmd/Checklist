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

class EditAdapter(private val title: String, viewModel: EditViewModel):
    RecyclerView.Adapter<EditViewHolder>() {

    //編集するチェックリストの中身をidと共に保存するためのリスト
    private val list = viewModel.editList

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
        if (list.size > position) {
            holder.edit.setText(list[position].item)
        }

        //編集内容をlistに代入するための関数
        holder.edit.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var removeValue: EditList? = null
                for (i in list) {
                    if (position == i.id) {
                        removeValue = i
                        break
                    }
                }
                if (removeValue != null) list.remove(removeValue)
                list.add(EditList(position, p0.toString()))
            }
        })
    }

    //LiveDataの内容をEditAdapterのインスタンスのlistに反映させる関数
    internal fun setInfoList(lists: MutableList<InfoList>){
        for (i in lists){
            if(i.title == title) list.add(EditList(list.size, i.item))
        }
        notifyDataSetChanged()
    }
}