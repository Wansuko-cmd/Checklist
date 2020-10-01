package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.EditList

class EditViewModel(application: Application) : AndroidViewModel(application) {
    val editList: MutableLiveData<MutableList<InfoList>> by lazy{
        MutableLiveData<MutableList<InfoList>>()
    }
    init{
        editList.value = mutableListOf()
    }

    fun insert(List: InfoList){
        editList.value!!.add(List)
    }

    fun changeItem(position: Int, Item: String){
        editList.value!![position].item = Item
    }

    fun changeCheck(position: Int, check: Boolean){
        editList.value!![position].check = check
    }

    fun update(list: MutableList<InfoList>){
        editList.value = list
    }
}