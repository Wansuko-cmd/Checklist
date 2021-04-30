package com.wsr.shopping_friend.contents

import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.share.view_model.AppViewModel
import com.wsr.shopping_friend.share.view_model.EditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//ドラッグやスワイプ時の処理を記述するためのclass
class ContentsItemTouchHelper(
    private val viewModel: AppViewModel,
    private val editViewModel: EditViewModel,
    private val showContentsAdapter: ContentsAdapter,
    private val snackBar: Snackbar
) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
    ) {

    //処理の内容がドラッグなのかを判定するための変数
    private var dragChecker: Boolean = false
    private var listForMoving = mutableListOf<InfoList>()

    //ドラッグなのかスワイプなのかを判断して設定をする処理
    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        super.onSelectedChanged(viewHolder, actionState)

        //ドラッグ時の処理
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder is ContentsViewHolder) {

            //ドラッグされている要素を赤色にする処理
            viewHolder.view.setBackgroundColor(Color.parseColor("#FFD5EC"))

            //編集用のリストに、editViewModelにある現状のlistを代入
            listForMoving = editViewModel.list

            //処理の内容がドラッグだったことをclearViewに伝える為にtrueを代入
            dragChecker = true
        }
    }

    //要素を移動させる処理
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        //移動させようとしている要素にチェックがついていないかを確認
        if (viewHolder is ContentsViewHolder
            && target is ContentsViewHolder
            && !viewHolder.check.isChecked
            && !target.check.isChecked
        ) {

            //どこからどこへと移動したのかを代入
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            //移動した要素の、並び順を入れ替える処理
            val fromValue = listForMoving[fromPosition]
            listForMoving[fromPosition] = listForMoving[toPosition].copy(number = fromValue.number)
            listForMoving[toPosition] = fromValue.copy(number = listForMoving[toPosition].number)

            //移動したことをadapterに通知
            showContentsAdapter.notifyItemMoved(toPosition, fromPosition)
        }
        return false
    }//アイコンを入れる

    //スワイプで削除する処理
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        //編集用のリストに、editViewModelにある現状のlistを代入
        val listForSwipe = editViewModel.list

        //削除する要素のindexを代入
        val index = viewHolder.adapterPosition

        //データから要素を削除して、一時保存する変数に代入する処理
        listForSwipe.removeAt(index).let {
            editViewModel.deleteValue = it
            runBlocking {
                viewModel.delete(it)
            }
        }
        editViewModel.list = listForSwipe

        //削除したことをadapterに通知
        showContentsAdapter.notifyItemRemoved(index)

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

        //ドラッグ処理だった時に行う処理
        if (dragChecker) {

            //editViewModelのlistに、変更点を代入。反映された後でリスト全体にリセットをかける
            editViewModel.list = listForMoving
            GlobalScope.launch(Dispatchers.Main) {
                editViewModel.checkData({ it == listForMoving }) { showContentsAdapter.notifyDataSetChanged() }
            }

            //チェッカーを元に戻す
            dragChecker = false
        }
        super.clearView(recyclerView, viewHolder)
    }
}