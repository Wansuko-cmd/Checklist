package com.wsr.shopping_friend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.view_holder.ListViewHolder
import com.wsr.shopping_friend.databinding.AddChecklistBinding
import com.wsr.shopping_friend.fragments.ShowContentsFragment
import com.wsr.shopping_friend.preference.getTextSize
import com.wsr.shopping_friend.view_model.EditViewModel

//リストの内容を見せるRecyclerViewのためのアダプター
class ListAdapter(
    private val context: Context,
    private val editViewModel: EditViewModel,
    private val showContentsFragment: ShowContentsFragment
    ) : RecyclerView.Adapter<ListViewHolder>() {

    //使用する関数、変数の定義
    var focus = -1

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(AddChecklistBinding.inflate(inflater, parent, false))
    }

    //入っている要素の数を返す関数
    override fun getItemCount(): Int {
        return editViewModel.list.size
    }

    //ViewHolderのインスタンスの保持する値を変更
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val id = editViewModel.list[position].id

        //設定の内容を反映するための処理
        holder.apply{

            setBind(editViewModel, editViewModel.list[position].id)

            check.setOnClickListener {
                val oldIndex = editViewModel.list.indexOfFirst { it.id == id }
                val newIndex = editViewModel.list.sortedBy { it.number }.sortedBy { it.check }.indexOfFirst { it.id == id }
                setColor(editViewModel, id)
                notifyItemMoved(oldIndex, newIndex)
                editViewModel.list = editViewModel.list
            }

            item.apply{

                textSize = getTextSize(context).toFloat()

                setOnLongClickListener {
                    view.setBackgroundColor(Color.parseColor("#FFD5EC"))
                    true
                }

                setOnEditorActionListener { _, i, _ ->
                    //Enterを押したとき、i == 0　となる。EditorInfo.IME_ACTION_DONEは違う値なので注意
                    if(i == 0 && editViewModel.list.sortedBy { it.check }.size == adapterPosition + 1){
                        showContentsFragment.addElements()
                        true
                    }else{
                        false
                    }
                }

                if(adapterPosition == focus){
                    requestFocus()
                }
            }
        }

        /*

        //focusを当てる処理
        if(holder.adapterPosition == focus){
            holder.item.requestFocus()
        }

         */
    }
}