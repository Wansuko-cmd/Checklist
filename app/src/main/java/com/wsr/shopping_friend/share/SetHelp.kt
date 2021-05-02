package com.wsr.shopping_friend.share

import android.content.Context
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.share.view_model.EditViewModel
import java.util.*

//ヘルプ画面を用意するための関数
fun setHelp(context: Context, editViewModel: EditViewModel) {

    //string.xmlに設定している文章を代入
    val helpList = context.resources.getStringArray(R.array.help)

    //臨時のリストに、helpListの中身を組み込んだ要素のリストを代入
    val tempList = helpList.mapIndexed { index, item ->
        InfoList(UUID.randomUUID().toString(), index, "", false, item)
    } as MutableList<InfoList>

    //editViewModelに適用
    editViewModel.list = tempList
}