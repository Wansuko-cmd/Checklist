package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.EditList

class EditViewModel(application: Application) : AndroidViewModel(application) {
    /*val editList: MutableLiveData<MutableList<InfoList>> by lazy{
        MutableLiveData<MutableList<InfoList>>()
    }
    init{
        editList.value = mutableListOf()
    }*/

    var editList: MutableList<InfoList> = mutableListOf()

    val setPosition: (String) -> Int = fun(id: String): Int{
        for ((count, i) in editList.withIndex()){
            if(id == i.id) return count
        }
        return -1
    }

    fun insert(List: InfoList){
        editList.add(List)
    }

    fun changeItem(id: String, Item: String){
        editList[setPosition(id)] = editList[setPosition(id)].copy(item = Item)
    }

    /*fun changeCheck(position: Int, Check: Boolean){
        editList[position] = editList[position].copy(check = Check)
    }*/
    fun update(list: MutableList<InfoList>){
        editList = list
    }
}