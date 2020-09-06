package com.wsr.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.R
import com.wsr.checklist.type_file.EditList
import com.wsr.checklist.view_holder.EditViewHolder
import com.wsr.checklist.view_holder.ListViewHolder
import java.util.*

class EditAdapter():
    RecyclerView.Adapter<EditViewHolder>(){

    private val list = mutableListOf<EditList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.edit_contents, parent, false)
        return EditViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(list.size <= 0){
            return 1
        }
        return  list.size
    }

    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        holder.edit.hint = "Item"
        /*if (holder.edit.text != null && holder.edit.text.toString() != ""){
            list.add(EditList(position, holder.edit.text.toString()))
        }*/
    }
}