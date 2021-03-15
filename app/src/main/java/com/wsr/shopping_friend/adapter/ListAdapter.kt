package com.wsr.shopping_friend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.view_holder.ListViewHolder
import com.wsr.shopping_friend.databinding.AddChecklistBinding
import com.wsr.shopping_friend.preference.getTextSize
import com.wsr.shopping_friend.type_file.CustomTextWatcher
import com.wsr.shopping_friend.view_model.EditViewModel

//リストの内容を見せるRecyclerViewのためのアダプター
class ListAdapter(
    private val context: Context,
    private val editViewModel: EditViewModel):
    RecyclerView.Adapter<ListViewHolder>() {

    //使用する関数、変数の定義
    var changeText: (p0: String, position: Int) -> Unit = {_,_  ->}
    var changeCheck: (check: Boolean, holder: ListViewHolder) -> Unit = {_, _ ->}
    var deleteElement: (position: Int) -> Unit = {}
    var focus = -1

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(AddChecklistBinding.inflate(inflater, parent, false))
    }

    //入っている要素の数を返す関数
    override fun getItemCount(): Int {
        return editViewModel.getList().size
    }

    //ViewHolderのインスタンスの保持する値を変更
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //設定の内容を反映するための処理
        holder.item.textSize = getTextSize(context).toFloat()

        holder.setBind(editViewModel)

        /*
        //editViewModelの情報を格納するためのプロセス
        for (i in editViewModel.getList()) {
            if (holder.adapterPosition == i.number) {
                holder.check.isChecked = editViewModel.getCheck(i.id)
                holder.item.setText(editViewModel.getItem(i.id))
                break
            }
        }

        //focusを当てる処理
        if(holder.adapterPosition == focus){
            holder.item.requestFocus()
        }

        //チェックのついてないところを色付けするためのプロセス
        if (holder.check.isChecked) {
            holder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
        }

        //アイテムが変更されたときにeditViewModelの保持する値を変更するためのプロセス
        holder.item.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                changeText(p0.toString(), holder.adapterPosition)
            }
        })

        holder.item.setOnLongClickListener{
            holder.view.setBackgroundColor(Color.parseColor("#FFD5EC"))
            true
        }

        //チェックの状態が変更したときにeditViewModelに保存するためのプロセス
        holder.check.setOnClickListener {
            changeCheck(holder.check.isChecked, holder)
        }

         */
    }
}