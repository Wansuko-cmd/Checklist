package com.wsr.shopping_friend.view_holder

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.AddChecklistBinding
import com.wsr.shopping_friend.view_model.EditViewModel

//ListAdapterのためのホルダー
class ListViewHolder(private val binding: AddChecklistBinding): RecyclerView.ViewHolder(binding.root){
    val item: EditText = binding.Item

    fun setBind(editViewModel: EditViewModel){
        binding.run{
            this.editViewModel = editViewModel
            this.index = 1
        }
    }
}

