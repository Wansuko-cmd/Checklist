package com.wsr.shopping_friend.view_holder

import android.graphics.Color
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.AddChecklistBinding
import com.wsr.shopping_friend.view_model.EditViewModel

//ListAdapterのためのホルダー
class ListViewHolder(private val binding: AddChecklistBinding): RecyclerView.ViewHolder(binding.root) {
    val item: EditText = binding.Item
    val check = binding.CheckBox
    val view = binding.root

    fun setBind(editViewModel: EditViewModel, id: String, listAdapter: com.wsr.shopping_friend.adapter.ListAdapter) {
        binding.run {

            this.value = editViewModel.list[editViewModel.list.indexOfFirst { it.id == id }]
            setColor(editViewModel, id)

            check.setOnClickListener {
                val oldIndex = editViewModel.list.indexOfFirst { it.id == id }
                val newIndex = editViewModel.list.sortedBy { it.number }.sortedBy { it.check }.indexOfFirst { it.id == id }
                setColor(editViewModel, id)
                listAdapter.notifyItemMoved(oldIndex, newIndex)
                editViewModel.updateList()
            }

            item.setOnLongClickListener {
                root.setBackgroundColor(Color.parseColor("#FFD5EC"))
                true
            }
        }
    }

    //チェックの状態に合わせて色を変える処理
    private fun setColor(editViewModel: EditViewModel, id: String) {
        val index = editViewModel.list.indexOfFirst { it.id == id }
        binding.root.setBackgroundColor(
            if (editViewModel.list[index].check) Color.parseColor("#FFFFFF")
            else Color.parseColor("#AFEEEE")
        )
    }
}


