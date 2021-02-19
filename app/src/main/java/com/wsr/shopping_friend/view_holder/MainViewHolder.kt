package com.wsr.shopping_friend.view_holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.ShowTitleBinding

//MainAdapterのためのホルダー
class MainViewHolder(binding: ShowTitleBinding): RecyclerView.ViewHolder(binding.root) {
    val title: TextView = binding.title
    val delete: ImageButton = binding.deleteButton
}