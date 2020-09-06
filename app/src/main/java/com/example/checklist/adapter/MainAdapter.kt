package com.example.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.checklist.view_holder.MainViewHolder
import com.example.checklist.R
import com.example.checklist.info_list_database.InfoList

class MainAdapter():
    RecyclerView.Adapter<MainViewHolder>(){

    private var list = emptyList<InfoList>()

    var clickTitleOnListener: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.show_title, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.title.setOnClickListener{
            clickTitleOnListener()
        }
    }

    internal fun setInfoList(lists: MutableList<InfoList>){
        this.list = lists
        notifyDataSetChanged()
    }
}