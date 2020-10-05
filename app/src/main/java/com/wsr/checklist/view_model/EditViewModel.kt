package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wsr.checklist.info_list_database.InfoList

class EditViewModel(application: Application) : AndroidViewModel(application) {

    //InfoListをデータベースに共有せずに保持するための変数
    private var editList: MutableList<InfoList> = mutableListOf()

    //idを入れることでListのポジションを返り値に持つ変数
    val setPosition: (String) -> Int = fun(id: String): Int{
        for ((count, i) in editList.withIndex()){
            if(id == i.id) return count
        }
        return -1
    }

    //新しいデータを代入するための関数
    fun insert(List: InfoList){
        editList.add(List)
        sortTrueFalse(editList)
    }

    //アイテムを変更するための変数
    fun changeItem(id: String, Item: String){
        editList[setPosition(id)] = editList[setPosition(id)].copy(item = Item)
        sortTrueFalse(editList)
    }

    //チェックの状態を変更するための関数
    fun changeCheck(id: String, Check: Boolean){
        editList[setPosition(id)] = editList[setPosition(id)].copy(check = Check)
        sortTrueFalse(editList)
    }

    //ここに保存されているリストを返り値に持つ関数
    fun getList(): MutableList<InfoList>{
        return editList
    }

    //ここに保存されているリストを、InfoListのcheckに合わせてFalse　->　Trueの順番に並べるための関数
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