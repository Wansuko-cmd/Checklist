package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.RecordNumber

class EditViewModel(application: Application) : AndroidViewModel(application) {

    //InfoListをデータベースに共有せずに保持するための変数
    private val numList: MutableList<RecordNumber> = mutableListOf()
    private val editList: MutableList<InfoList> = mutableListOf()

    //idを入れることでListのポジションを返り値に持つ変数
    val setPosition: (String) -> Int = fun(id: String): Int {
        for ((count, i) in editList.withIndex()) {
            if (id == i.id) return count
        }
        return -1
    }

    //新しいデータを代入するための関数
    fun insert(List: InfoList) {
        numList.add(RecordNumber(List.id, List.number))
        editList.add(List)
        sortTrueFalse(editList)
    }

    //アイテムを変更するための変数
    fun changeItem(id: String, Item: String) {
        editList[setPosition(id)] = editList[setPosition(id)].copy(item = Item)
        sortTrueFalse(editList)
    }

    //チェックの状態を変更するための関数
    fun changeCheck(id: String, Check: Boolean) {
        editList[setPosition(id)] = editList[setPosition(id)].copy(check = Check)
        sortTrueFalse(editList)
    }

    //ここに保存されているリストを返り値に持つ関数

    fun getList(): MutableList<InfoList> {
        return editList
    }

    fun getNumList(): MutableList<RecordNumber> {
        return numList
    }

    fun getItem(id: String): String{
        return editList[setPosition(id)].item
    }

    fun getCheck(id: String): Boolean{
        return editList[setPosition(id)].check
    }

    fun delete(id: String){
        numList.removeAll{it.id == id}
        numList.sortBy { it.number }
        for ((count, i) in numList.withIndex()){
            numList[count] = numList[count].copy(number = count)
        }
        editList.removeAll{it.id == id}
        for(i in numList){
            editList[setPosition(i.id)] = editList[setPosition(i.id)].copy(number = i.number)
        }
    }

    fun checkEmpty(id: String): Boolean {
        when {
            setPosition(id) == editList.size - 1 -> return true
            /*setPosition(id) > editList.size - 1 -> return false
            setPosition(id) < editList.size - 1 -> {
                if(editList[setPosition(id) + 1].item != ""){
                    return true
                }
            }*/
        }
        return false
    }

    //ここに保存されているリストを、InfoListのcheckに合わせてFalse　->　Trueの順番に並べるための関数
    private fun sortTrueFalse(list: List<InfoList>): List<InfoList> {
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