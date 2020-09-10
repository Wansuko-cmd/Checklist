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
    var list = viewModel.editList
    private var numList = mutableListOf<Int>()

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
                list.sortBy{it.id}
                holder.edit.text = list[position].item
            }
        var check = false
        for ( i in list){
            if(position == i.id){
                list[position] = list[position].copy(item = holder.edit.text.toString())
                check = true
            }
        }
        if(!check){
            list.add(EditList(position, holder.edit.text.toString()))
        }


        //編集内容をlistに代入するための関数
        holder.edit.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var exist = false
                for (i in numList) {
                    if (position == i) {
                        exist = true
                    }
                }
                if (!exist) numList.add(position)
            }
        })
    }

    //LiveDataの内容をEditAdapterのインスタンスのlistに反映させる関数
    internal fun setInfoList(lists: MutableList<InfoList>){
            for (i in lists){
                if(i.title == title){
                    list.add(EditList(i.number, i.item))
                    numList.add(i.number)
                }
            }
        list.sortBy { it.id }
        notifyDataSetChanged()
    }
}