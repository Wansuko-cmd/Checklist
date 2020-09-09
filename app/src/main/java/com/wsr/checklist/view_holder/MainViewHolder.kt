package com.wsr.checklist.view_holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_title.view.*

class MainViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val title: TextView = view.title
}