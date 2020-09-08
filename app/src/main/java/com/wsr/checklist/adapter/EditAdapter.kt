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
import java.util.*

class EditAdapter(private val title: String, private val viewModel: EditViewModel):
    RecyclerView.Adapter<EditViewHolder>() {

    private val list = viewModel.editList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.edit_contents, parent, false)
        return EditViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (list.size <= 0) {
            return 1
        }
        return list.size + 1
    }

    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        if (list.size > position) {
            holder.edit.setText(list[position].item)
        }
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

    internal fun setInfoList(lists: MutableList<InfoList>){
        val tempList = lists
        for (i in tempList){
            if(i.title == title) list.add(EditList(list.size, i.item))
        }
        notifyDataSetChanged()
    }
}