package com.wsr.checklist.type_file

import android.content.Context
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.view_model.AppViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

fun setHelp(context: Context, viewModel: AppViewModel) {
    /*val list: List<String> = listOf(
        context.getString(R.string.help_1),
        context.getString(R.string.help_2),
        context.getString(R.string.help_3),
        context.getString(R.string.help_4),
        context.getString(R.string.help_5),
        context.getString(R.string.help_6),
        context.getString(R.string.help_7),
        context.getString(R.string.help_8),
        context.getString(R.string.help_9),
        context.getString(R.string.help_10)
    )*/
    val list = context.resources.getStringArray(R.array.help)
    var dbList = viewModel.getHelp()
    while(dbList.size != list.size){
        viewModel.deleteWithTitle("")
        for ((count, item) in list.withIndex()) runBlocking {
            val job = GlobalScope.launch {
                viewModel.insert(InfoList(UUID.randomUUID().toString(), count, "", false, item))
            }
            job.join()
        }
        dbList = viewModel.getHelp()
    }
}