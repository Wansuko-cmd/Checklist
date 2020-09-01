package com.example.checklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListAdapter(private val viewModel: AppViewModel):
    RecyclerView.Adapter<ListViewHolder>(){

    //InfoListDatabaseから流れてくるLiveDataをここに代入して、ここのインスタンスに活用
    private var list = emptyList<InfoList>()

    //ViewHolderのインスタンス化
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        return ListViewHolder(view)
    }

    //listの大きさを返り値に持つ関数
    override fun getItemCount(): Int {
        return  list.size
    }

    //ViewHolderのインスタンスに情報を伝達？
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.view.CheckBox.isChecked = list[position].check
        holder.view.Item.text = list[position].item
        holder.check.setOnClickListener{
            viewModel.insert(holder.check.isChecked, list[position].item)
        }
    }

    //listにInfoListDatabaseから流れてきたLiveDataを代入
    internal fun setList(infoList: MutableList<InfoList>){
        this.list = infoList
        notifyDataSetChanged()
    }
}