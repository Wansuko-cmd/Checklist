package com.wsr.checklist.view_model

import android.app.Application
import android.icu.text.IDNA
import androidx.lifecycle.AndroidViewModel
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.RecordNumber

class EditViewModel(application: Application) : AndroidViewModel(application) {

    //InfoListをデータベースに共有せずに保持するための変数
    private val numList: MutableList<RecordNumber> = mutableListOf()
    private var editList: MutableList<InfoList> = mutableListOf()

    private var deleteItem: MutableList<InfoList> = mutableListOf()

    //idを入れることでListのポジションを返り値に持つ変数
    val setPosition: (String) -> Int = fun(id: String): Int {
        for ((count, i) in editList.withIndex()) {
            if (id == i.id) return count
        }
        return -1
    }

    //idを入れることでNumberを返り値に持つ関数
    val setNumber: (String) -> Int = fun(id: String): Int{
        for (i in editList){
            if (id == i.id) return i.number
        }
        return -1
    }

    fun nonCheckNumber(): Int{
        val list = editList.filter { !it.check }
        return list.size - 1
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

    //ここに保存されている、本来の数字の並びを保存するリストを返り値に持つ関数
    fun getNumList(): MutableList<RecordNumber> {
        return numList
    }

    //idと結びついている要素のアイテムを返す関数
    fun getItem(id: String): String{
        return editList[setPosition(id)].item
    }

    //idと結びついている要素のチェックの状態を返す関数
    fun getCheck(id: String): Boolean{
        return editList[setPosition(id)].check
    }

    //idで指定された要素を消す関数
    fun delete(id: String){
        numList.removeAll{it.id == id}
        numList.sortBy { it.number }
        for ((count, _) in numList.withIndex()){
            numList[count] = numList[count].copy(number = count)
        }
        deleteItem = editList
        editList.removeAll{it.id == id}
        for(i in numList){
            editList[setPosition(i.id)] = editList[setPosition(i.id)].copy(number = i.number)
        }
        sortTrueFalse(editList)
    }

    fun backDeleteItem(){
        if(deleteItem != mutableListOf<InfoList>()){
            editList = deleteItem
        }
    }

    //指定された要素の次の要素が何もないか確認するための関数
    fun checkEmpty(id: String): Boolean {
        if(setPosition(id) == editList.size - 1) return true
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