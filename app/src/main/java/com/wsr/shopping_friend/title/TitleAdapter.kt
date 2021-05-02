package com.wsr.shopping_friend.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.databinding.ChecklistTitleBinding
import com.wsr.shopping_friend.preference.getTextSize

//リストのタイトルを並べるRecyclerViewのためのアダプター
class TitleAdapter:
    RecyclerView.Adapter<TitleViewHolder>(){

    //全てのタイトル名を保存するリスト
    lateinit var titleList: MutableList<String>

    //使用する関数の定義
    lateinit var clickTitleOnListener: (title: String) -> Unit
    lateinit var clickDeleteOnListener: (title: String, position: Int) -> Unit

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TitleViewHolder(ChecklistTitleBinding.inflate(inflater, parent, false))
    }

    //titleListの長さを返す関数
    override fun getItemCount(): Int {
        return  titleList.size
    }

    //インスタンス化したViewHolderの中の値の変更
    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {

        //要素の文字の部分の設定
        holder.title.apply {
            text = titleList[position]
            textSize = getTextSize(context).toFloat()

            //タイトル名をクリックした際の処理
            setOnClickListener {
                clickTitleOnListener(holder.title.text.toString())
            }
        }

        //deleteボタンを押した際の処理
        holder.delete.setOnClickListener {
            clickDeleteOnListener(titleList[holder.bindingAdapterPosition], holder.bindingAdapterPosition)
        }
    }

    //LiveDataの内容をMainAdapterのインスタンスに反映させる関数
    internal fun setInfoList(lists: MutableList<InfoList>){

        /*登録されている要素の中で以下の要素のみを抽出してタイトル名を保存するリストに代入
        *
        * ・重複していない
        * ・タイトル名が空ではない
        *
        * */
        titleList = lists
            .asSequence()
            .map { it.title }
            .distinct()
            .filter { it != "" }
            .sorted()
            .toMutableList()

        notifyDataSetChanged()
    }
}