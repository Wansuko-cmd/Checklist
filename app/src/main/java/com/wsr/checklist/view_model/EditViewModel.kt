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
            val range = editList.value!!.size - 1
            editList.value!![position] = editList.value!![position].copy(item = Item)
            /*if (Item == "" && position != range){
                editList.value!!.sortBy{it.id}
                editList.value!!.removeAt(position)
                for (i in 0 until range){
                    editList.value!![i] = editList.value!![i].copy(id = i)
                }
            }else if(position < range){
                for (i in position..range){
                    editList.value!![i] = editList.value!![i].copy(id = i + 1)
                }
                editList.value!!.add(EditList(position, Item))
                editList.value!!.sortBy{it.id}
            }
            else{
                editList.value!![position] = editList.value!![position].copy(item = Item)
            }*/
        }
    }
}