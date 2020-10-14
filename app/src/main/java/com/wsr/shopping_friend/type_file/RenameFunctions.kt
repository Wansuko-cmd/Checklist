package com.wsr.shopping_friend.type_file

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.preference.getDefaultTitle

//タイトル名を変更する際に呼び出される関数
fun renameAlert(context: Context, Function: (String) -> Unit, titleList: List<String>, setTitle: String){
    val defaultTitle = getDefaultTitle(context)
    val editText = EditText(context)
    editText.setText(setTitle)
    AlertDialog.Builder(context)
        .setTitle(R.string.edit_title_title)
        .setMessage(R.string.edit_title_message)
        .setView(editText)
        .setPositiveButton(R.string.edit_title_positive) { _, _ ->
            //新しく作成するチェックリストのタイトルの入った変数
            val title = when(editText.text.toString()){
                "" -> checkTitle(defaultTitle, titleList)
                setTitle -> setTitle
                else -> checkTitle(editText.text.toString(), titleList)
            }

            //渡された関数を実行
            Function(title)
        }
        .setNegativeButton(R.string.edit_title_negative, null)
        .setCancelable(false)
        .show()
}

//タイトル名がかぶっていないかを確認する処理
fun checkTitle(Title: String, titleList: List<String>): String{
    var title = Title
    if (title == "") title = "Non-Title"
    for (i in titleList) {
        if (title == i) title = addNumber(title, titleList)
    }
    return title
}

//タイトルの後ろに、同じ名前にならないように数字をつける関数
fun addNumber(title: String, titleList: List<String>) : String{
    var tempTitle: String
    var num = 1
    while(true) {
        var result = true
        tempTitle = "$title($num)"
        for (i in titleList) {
            if (tempTitle == i) result = false
        }
        if (result) break
        num++
    }
    return tempTitle
}