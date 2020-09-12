package com.wsr.checklist.view_holder

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edit_contents.view.*

class EditViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val edit: EditText = view.edit_contents
}