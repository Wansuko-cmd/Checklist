package com.example.checklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListAdapter(private val viewModel: AppViewModel):
    RecyclerView.Adapter<ListViewHolder>(){

    //private val list = viewModel.infoList
    private var list = emptyList<InfoList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.CheckBox.isChecked = list[position].check
        holder.item.text = list[position].item
        holder.check.setOnClickListener{
            viewModel.changeCheck(holder.check.isChecked, list[position].item)
            //viewModel.insert(InfoList(holder.check.isChecked, list[position].item))
        }
    }

    internal fun setInfoList(lists: MutableList<InfoList>){
        this.list = lists
        notifyDataSetChanged()
    }

}