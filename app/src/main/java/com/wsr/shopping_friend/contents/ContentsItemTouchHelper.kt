package com.wsr.shopping_friend.contents

import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.share.view_model.AppViewModel
import com.wsr.shopping_friend.share.view_model.EditViewModel
import kotlinx.coroutines.*

//ドラッグやスワイプ時の処理を記述するためのclass
class ContentsItemTouchHelper(
    private val appViewModel: AppViewModel,
    private val editViewModel: EditViewModel,
    private val contentsAdapter: ContentsAdapter,
    private val snackBar: Snackbar,
    private val lifecycleScope: CoroutineScope
) : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
    ) {

    //処理の内容がドラッグなのかを判定するための変数
    private var dragChecker: Boolean = false

    //処理内容を一時保存するための変数
    private var tempList = mutableListOf<InfoList>()


    //ドラッグなのかスワイプなのかを判断して設定をする処理
    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        super.onSelectedChanged(viewHolder, actionState)

        if (viewHolder is ContentsViewHolder) {

            //編集用のリストに、editViewModelにある現状のlistを代入
            tempList = editViewModel.list

            //ドラッグ時の処理
            if (
                actionState == ItemTouchHelper.ACTION_STATE_DRAG &&
                !viewHolder.check.isChecked
            ) {
                //ドラッグされている要素を赤色にする処理
                viewHolder.view.setBackgroundColor(Color.parseColor("#FFD5EC"))

                //処理の内容がドラッグだったことをclearViewに伝える為にtrueを代入
                dragChecker = true
            }
        }
    }

    //要素を移動させる処理
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        //移動させようとしている要素にチェックがついていないかを確認
        if (
            viewHolder is ContentsViewHolder &&
            target is ContentsViewHolder &&
            !viewHolder.check.isChecked &&
            !target.check.isChecked
        ) {

            //どこからどこへと移動したのかを代入
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition

            if(fromPosition != toPosition){
                //移動した要素の、並び順を入れ替える処理
                val fromValue = tempList[fromPosition]

                tempList[fromPosition] = tempList[toPosition].copy(number = fromValue.number)
                tempList[toPosition] = fromValue.copy(number = tempList[toPosition].number)

                //移動したことをadapterに通知
                contentsAdapter.notifyItemMoved(fromPosition, toPosition)

            }


        }
        return false
    }

    //スワイプで削除する処理
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        //削除する要素のindexを代入
        val index = viewHolder.bindingAdapterPosition

        tempList.removeAt(index).let {
            editViewModel.deleteValue = it

            //処理を待つ必要あり
            runBlocking {
                //それぞれのViewModelに変更を通達
                appViewModel.delete(it)
                editViewModel.setList(tempList)
            }
        }

        //削除したことをadapterに通知
        contentsAdapter.notifyItemRemoved(index)

        //UNDOをするためのsnackBarを表示
        snackBar.show()
    }

    //ドラッグやスワイプの処理が終了した時に呼び出される処理
    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {

        //ドラッグやスワイプした要素に決められた色をつける
        if (viewHolder is ContentsViewHolder) {
            if (viewHolder.check.isChecked) {
                viewHolder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                viewHolder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
            }
        }

        if (dragChecker) {

            lifecycleScope.launch {

                //editViewModelのリストに変更を通知
                editViewModel.setList(tempList)

                //Adapterに変更を通知
                contentsAdapter.notifyDataSetChanged()

                //チェッカーを元に戻す
                dragChecker = false
            }
        }


        super.clearView(recyclerView, viewHolder)
    }
}
