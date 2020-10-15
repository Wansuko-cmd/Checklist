package com.wsr.shopping_friend.type_file

import android.content.Context
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.view_model.EditViewModel
import java.util.*

//ヘルプ画面を用意するための関数
fun setHelp(context: Context, editViewModel: EditViewModel) {
    val helpList = context.resources.getStringArray(R.array.help)
    for((count, i) in helpList.withIndex()){
        editViewModel.insert(InfoList(UUID.randomUUID().toString(), count, "", false, i))
    }
}