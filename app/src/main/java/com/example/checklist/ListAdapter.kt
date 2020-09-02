package com.example.checklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListAdapter(private val viewModel: AppViewModel):
    RecyclerView.Adapter<ListViewHolder>(){

    //private val list = viewModel.infoList
    private var list = emptyList<SampleData>()

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
        holder.view.Item.text = list[position].item
        holder.check.setOnClickListener{
            viewModel.insert(holder)
        }
    }

    internal fun setInfoList(lists: MutableList<SampleData>){
        this.list = lists
        notifyDataSetChanged()
    }

}