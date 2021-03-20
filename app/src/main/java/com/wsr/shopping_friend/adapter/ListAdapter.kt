package com.wsr.shopping_friend.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.view_holder.ListViewHolder
import com.wsr.shopping_friend.databinding.ChecklistColumnBinding
import com.wsr.shopping_friend.fragments.ShowContentsFragment
import com.wsr.shopping_friend.preference.getTextSize
import com.wsr.shopping_friend.view_model.EditViewModel

//リストの内容を見せるRecyclerViewのためのアダプター
class ListAdapter(
    private val editViewModel: EditViewModel,
    private val showContentsFragment: ShowContentsFragment
    ) : RecyclerView.Adapter<ListViewHolder>() {

    //使用する関数、変数の定義
    var focus = -1

    var scrollToPosition: (Int) -> Unit = {}

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(ChecklistColumnBinding.inflate(inflater, parent, false))
    }

    //入っている要素の数を返す関数
    override fun getItemCount(): Int {
        return editViewModel.list.size
    }

    //ViewHolderのインスタンスの保持する値を変更
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val id = editViewModel.list[position].id

        //設定の内容を反映するための処理
        holder.apply{

            setBind(editViewModel, editViewModel.list[position].id)

            //チェックが入ったときに入れ替えたり色を付けたりする処理
            check.setOnClickListener {
                val oldIndex = editViewModel.list.indexOfFirst { it.id == id }
                val newIndex = editViewModel.list.sortedBy { it.number }.sortedBy { it.check }.indexOfFirst { it.id == id }
                setColor(editViewModel, id)
                notifyItemMoved(oldIndex, newIndex)
                editViewModel.list = editViewModel.list
                scrollToPosition(oldIndex)
            }

            //EditTextに関する処理をする部分
            item.apply{

                //設定した文字サイズを反映させる処理
                textSize = getTextSize(context).toFloat()

                //長押し時に色を変える処理
                setOnLongClickListener {
                    view.setBackgroundColor(Color.parseColor("#FFD5EC"))
                    true
                }

                //Enterの入力を検出する処理
                setOnEditorActionListener { _, i, _ ->
                    if(
                        i == EditorInfo.IME_ACTION_DONE ||
                        (i == EditorInfo.IME_ACTION_NEXT && editViewModel.list.filter { !it.check }.size == adapterPosition + 1)){
                        showContentsFragment.addElements()
                        true
                    }else{
                        false
                    }
                }

                //Focusを当てる処理
                if(adapterPosition == focus){
                    requestFocus()
                }
            }
        }
    }
}