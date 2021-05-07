package com.wsr.shopping_friend.share.repository

import androidx.lifecycle.LiveData
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.database.InfoListDao

class AppRepository(private val infoListDao: InfoListDao) {

    //データベースに保存されている全てのデータ
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    //ローカルデータベースへの操作内容

    //データを一つずつ挿入するための関数
    suspend fun insert(infoList: InfoList){
        infoListDao.insert(infoList)
    }

    //データをアップデートするための関数
    suspend fun update(infoList: MutableList<InfoList>){
        val list = infoListDao.update(infoList)
    }

    //タイトルを変更するための関数
    suspend fun changeTitle(oldTitle: String, newTitle: String){
        infoListDao.changeTitle(oldTitle, newTitle)
    }

    //特定の要素を削除する関数
    suspend fun delete(infoList: InfoList){
        infoListDao.delete(infoList)
    }

    //リストに記載されている要素を削除する関数
    suspend fun deleteList(infoList: MutableList<InfoList>){
        infoListDao.deleteList(infoList)
    }

    //指定したタイトル名のものを削除する関数
    suspend fun deleteWithTitle(title:String){
        infoListDao.deleteWithTitle(title)
    }
}