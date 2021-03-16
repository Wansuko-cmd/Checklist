package com.wsr.shopping_friend.view_holder

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.AddChecklistBinding
import com.wsr.shopping_friend.view_model.EditViewModel

//ListAdapterのためのホルダー
class ListViewHolder(private val binding: AddChecklistBinding): RecyclerView.ViewHolder(binding.root) {
    val item: EditText = binding.Item
    val check = binding.CheckBox
    val view = binding.root

    fun setBind(editViewModel: EditViewModel, index: Int) {
        binding.run {
            this.editViewModel = editViewModel
            this.index = index
            setColor(editViewModel, index)

            check.setOnClickListener {
                setColor(editViewModel, index)
            }
        }
    }

    private fun setColor(editViewModel: EditViewModel, index: Int) {
        binding.run {
            editViewModel.list.value?.let {
                root.setBackgroundColor(
                    if (it[index].check) Color.parseColor("#FFFFFF")
                    else Color.parseColor("#AFEEEE")
                )
            }
        }
    }
}

