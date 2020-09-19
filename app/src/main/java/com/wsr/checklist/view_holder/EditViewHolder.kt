package com.wsr.checklist.view_holder

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edit_contents.view.*

class EditViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val edit: EditText = view.edit_contents
    val delete: ImageButton = view.delete_button_for_edit
}