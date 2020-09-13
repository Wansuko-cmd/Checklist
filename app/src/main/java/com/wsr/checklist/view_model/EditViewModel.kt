package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wsr.checklist.type_file.EditList

class EditViewModel(application: Application) : AndroidViewModel(application) {
    val editList: MutableLiveData<MutableList<EditList>> by lazy{
        MutableLiveData<MutableList<EditList>>()
    }
    init{
        editList.value = mutableListOf()
    }

    fun insert(List: EditList){
        editList.value!!.add(List)
    }

    fun changeItem(position: Int, Item: String){
        if(editList.value != null){
            editList.value!![position] = editList.value!![position].copy(item = Item)
        }
    }
}