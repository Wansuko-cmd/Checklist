package com.wsr.checklist.type_file

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.wsr.checklist.R
import java.util.*

fun renameAlert(context: Context, Function: (String) -> Unit, titleList: List<String>, setTitle: String){
    val editText = EditText(context)
    editText.setText(setTitle)
    AlertDialog.Builder(context)
        .setTitle(R.string.edit_title_title)
        .setMessage(R.string.edit_title_message)
        .setView(editText)
        .setPositiveButton(R.string.edit_title_positive) { _, _ ->
            //新しく作成するチェックリストのタイトルの入った変数
            val title = if(
                (editText.text.toString() == setTitle
                        &&editText.text.toString() != "")
                    ) setTitle else checkTitle(editText.text.toString(), titleList)

            //渡された関数を実行
            Function(title)
        }
        .setNegativeButton(R.string.edit_title_negative, null)
        .setCancelable(false)
        .show()
}

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