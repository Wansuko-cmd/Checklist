package com.wsr.shopping_friend.title

import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.ChecklistTitleBinding

//MainAdapterのためのホルダー
class TitleViewHolder(binding: ChecklistTitleBinding): RecyclerView.ViewHolder(binding.root) {
    val title: TextView = binding.title
    val delete: ImageButton = binding.deleteButton
}