package com.wsr.shopping_friend.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.databinding.ChecklistColumnBinding
import com.wsr.shopping_friend.preference.getTextSize
import com.wsr.shopping_friend.share.view_model.EditViewModel

//リストの内容を見せるRecyclerViewのためのアダプター
class ContentsAdapter(
    private val editViewModel: EditViewModel,
    private val contentsFragment: ContentsFragment
    ) : RecyclerView.Adapter<ContentsViewHolder>() {

    //focusを当てる場所を指定するための変数
    var focus = -1

    //引数で指定された位置までスクロールする関数
    var scrollToPosition: (Int) -> Unit = {}

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContentsViewHolder(ChecklistColumnBinding.inflate(inflater, parent, false))
    }

    //入っている要素の数を返す関数
    override fun getItemCount(): Int {
        return editViewModel.list.size
    }

    //ViewHolderのインスタンスの保持する値を変更
    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {

        val id = editViewModel.list[position].id

        //設定の内容を反映するための処理
        holder.apply{

            //初期化の処理
            setBind(editViewModel, editViewModel.list[position].id)

            //チェックが入ったときに入れ替えたり色を付けたりする処理
            check.setOnClickListener {

                //チェックが入る前と入った後のIndexを取得
                val oldIndex = editViewModel.list.indexOfFirst { it.id == id }
                val newIndex = editViewModel.list.sortedBy { it.number }.sortedBy { it.check }.indexOfFirst { it.id == id }

                //指定の位置まで移動させて、editViewModelのLiveDataを更新する
                notifyItemMoved(oldIndex, newIndex)
                editViewModel.list = editViewModel.list

                //色の設定と、スクロールの制限
                setColor(editViewModel, id)
                scrollToPosition(oldIndex)
            }

            //EditTextに関する処理をする部分
            item.apply{

                //設定した文字サイズを反映させる処理
                textSize = getTextSize(context).toFloat()

                //チェックのついていない要素の中で一番下の要素でEnterを押したときに要素を足す処理
                setOnEditorActionListener { _, i, _ ->
                    if(
                        i == EditorInfo.IME_ACTION_DONE ||
                        (i == EditorInfo.IME_ACTION_NEXT && editViewModel.list.filter { !it.check }.size == adapterPosition + 1)){
                        contentsFragment.addElement()
                        true
                    }else{
                        false
                    }
                }

                //focusで指定された所にFocusを当てる処理
                if(adapterPosition == focus){
                    requestFocus()
                }
            }
        }
    }
}