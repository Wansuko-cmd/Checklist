package com.wsr.shopping_friend.view_holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_title.view.*

//MainAdapterのためのホルダー
class MainViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val title: TextView = view.title
    val delete: ImageButton = view.delete_button
}