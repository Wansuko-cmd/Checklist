package com.wsr.shopping_friend.type_file

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback
    : ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT)) {

    //スワイプした時の処理
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}