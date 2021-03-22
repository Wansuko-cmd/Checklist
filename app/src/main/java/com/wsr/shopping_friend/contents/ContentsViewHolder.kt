package com.wsr.shopping_friend.contents

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.ChecklistColumnBinding
import com.wsr.shopping_friend.share.view_model.EditViewModel

//ListAdapterのためのホルダー
class ContentsViewHolder(private val binding: ChecklistColumnBinding): RecyclerView.ViewHolder(binding.root) {
    val item = binding.Item
    val check = binding.CheckBox
    val view = binding.root

    //要素の初期設定をする処理
    fun setBind(editViewModel: EditViewModel, id: String) {
        binding.run {
            this.value = editViewModel.list[editViewModel.list.indexOfFirst { it.id == id }]
            setColor(editViewModel, id)
            item.setHorizontallyScrolling(false)
            item.maxLines = Int.MAX_VALUE
        }
    }

    //チェックの状態に合わせて色を変える処理
    fun setColor(editViewModel: EditViewModel, id: String) {
        val index = editViewModel.list.indexOfFirst { it.id == id }
        binding.root.setBackgroundColor(
            if (editViewModel.list[index].check) Color.parseColor("#FFFFFF")
            else Color.parseColor("#AFEEEE")
        )
    }
}


