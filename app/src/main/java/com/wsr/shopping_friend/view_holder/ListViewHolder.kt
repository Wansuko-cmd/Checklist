package com.wsr.shopping_friend.view_holder

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.AddChecklistBinding
import com.wsr.shopping_friend.databinding.ShowPreferenceBinding

//ListAdapterのためのホルダー
class ListViewHolder(binding: AddChecklistBinding): RecyclerView.ViewHolder(binding.root){
    val check: CheckBox = binding.CheckBox
    val item: EditText = binding.Item
    val view: View = binding.root
}

