package com.example.checklist

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val check = view.CheckBox
    val item =view.Item
}

