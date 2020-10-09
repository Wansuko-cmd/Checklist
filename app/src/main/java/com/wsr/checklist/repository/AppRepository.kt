package com.wsr.checklist.repository

import androidx.lifecycle.LiveData
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.info_list_database.InfoListDao

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    //ローカルデータベースへの操作内容
    //データを挿入するための関数
    fun insert(infoList: InfoList){
        infoListDao.insert(infoList)
    }

    //チェックの状態を変更するための関数
    fun changeCheck(id: String, check: Boolean){
        infoListDao.changeCheck(id, check)
    }

    //タイトルを変更するための関数
    fun changeTitle(id: String, title: String){
        infoListDao.changeTitle(id, title)
    }

    //アイテムを変更するための関数
    fun changeItem(id: String, item: String){
        infoListDao.changeItem(id, item)
    }

    //指定したId名のものを削除する関数
    fun deleteWithId(id: String){
        infoListDao.deleteWithId(id)
    }

    //指定したタイトル名のものを削除する関数
    fun deleteWithTitle(title:String){
        infoListDao.deleteWithTitle(title)
    }
}