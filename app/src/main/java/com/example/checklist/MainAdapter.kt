package com.example.checklist

import android.content.Context
import android.content.Intent
import android.icu.text.IDNA
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

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