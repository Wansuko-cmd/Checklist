package com.wsr.shopping_friend.view_holder

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_checklist.view.*

class ListViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val check: CheckBox = view.CheckBox
    val item: EditText = view.Item
}

