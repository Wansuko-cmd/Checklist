package com.wsr.checklist.view_holder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val check: CheckBox = view.CheckBox
    val item: TextView =view.Item
}

