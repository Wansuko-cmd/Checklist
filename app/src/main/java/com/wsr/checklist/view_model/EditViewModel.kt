package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wsr.checklist.info_list_database.InfoList

class EditViewModel(application: Application) : AndroidViewModel(application) {

    private var editList: MutableList<InfoList> = mutableListOf()

    val setPosition: (String) -> Int = fun(id: String): Int{
        for ((count, i) in editList.withIndex()){
            if(id == i.id) return count
        }
        return -1
    }

    fun insert(List: InfoList){
        editList.add(List)
        sortTrueFalse(editList)
    }

    fun changeItem(id: String, Item: String){
        editList[setPosition(id)] = editList[setPosition(id)].copy(item = Item)
        sortTrueFalse(editList)
    }

    fun changeCheck(id: String, Check: Boolean){
        editList[setPosition(id)] = editList[setPosition(id)].copy(check = Check)
        sortTrueFalse(editList)
    }

    fun update(list: MutableList<InfoList>){
        editList = list
        sortTrueFalse(editList)
    }

    fun getList(): MutableList<InfoList>{
        return editList
    }



    private fun sortTrueFalse(list: List<InfoList>): List<InfoList>{
        list.sortedBy { it.number }
        val onlyFalseList: List<InfoList> = list.filter{ !it.check}
        val onlyTrueList: List<InfoList> = list.filter{ it.check }
        val result = onlyFalseList + onlyTrueList
        for ((num, i) in result.withIndex()){
            i.number = num
        }
        return result
    }
}