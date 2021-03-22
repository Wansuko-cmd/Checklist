package com.wsr.shopping_friend.share

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.preference.getDefaultTitle

//タイトル名を変更する際に呼び出される関数
fun renameTitle(context: Context, function: (String) -> Unit, titleList: List<String>, setTitle: String){

    //設定されているデフォルトタイトルの取得
    val defaultTitle = getDefaultTitle(context)

    //ダイアログの入力部分を担当するEditTextの設定
    val editText = EditText(context)
    editText.setHint(R.string.edit_title_hint)
    editText.setText(setTitle)

    //ダイアログの設定
    AlertDialog.Builder(context)
        .setTitle(R.string.edit_title_title)
        .setMessage(R.string.edit_title_message)
        .setView(editText)
        .setPositiveButton(R.string.edit_title_positive) { _, _ ->

            //新しく作成するチェックリストのタイトルの入った変数
            val title = when(val input = editText.text.toString()){

                //何も入力されなかったとき->デフォルトタイトル
                "" -> checkTitle(defaultTitle, titleList)

                //設定されているタイトルと同じとき->同じもの
                setTitle -> setTitle

                //それら以外->重複がないか確認
                else -> checkTitle(input, titleList)
            }

            //渡された関数を実行
            function(title)
        }
        .setNegativeButton(R.string.edit_title_negative, null)
        .setCancelable(false)
        .show()
}

//タイトル名がかぶっていないかを確認する処理
fun checkTitle(Title: String, titleList: List<String>): String{

    //返すタイトルを代入するための変数
    var title = Title

    //title名に何も入っていない場合はNon-Titleを返す
    if (title == "") title = "Non-Title"

    //同じ名前のタイトルがなくなるまで後ろに数字をつける
    while (titleList.contains(title)){
        title = addNumber(title, titleList)
    }
    return title
}

//タイトルの後ろに、同じ名前にならないように数字をつける関数
fun addNumber(title: String, titleList: List<String>) : String{

    //返すタイトルを代入するための変数
    var tempTitle: String = title

    //返すタイトルの後ろにつける数値を入れる変数
    var num = 1

    //タイトルが被らなくなるまで後ろの数値を変更する
    while(titleList.contains(tempTitle)) {
        tempTitle = "$title($num)"
        num++
    }
    return tempTitle
}