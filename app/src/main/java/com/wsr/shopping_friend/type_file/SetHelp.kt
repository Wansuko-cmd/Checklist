package com.wsr.shopping_friend.type_file

import android.content.Context
import com.wsr.shopping_friend.R
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.view_model.AppViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

fun setHelp(context: Context, viewModel: AppViewModel) {
    val helpList = context.resources.getStringArray(R.array.help)
    var dbList = viewModel.getHelp()
    while(dbList.size != helpList.size){
        viewModel.deleteWithTitle("")
        val list: MutableList<InfoList> = mutableListOf()
        for((count, item) in helpList.withIndex()){
            list.add(InfoList(UUID.randomUUID().toString(), count, "", false, item))
        }
        viewModel.insert(list)
        dbList = viewModel.getHelp()
    }
}