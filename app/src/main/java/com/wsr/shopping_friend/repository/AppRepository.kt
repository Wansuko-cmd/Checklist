package com.wsr.shopping_friend.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.info_list_database.InfoListDao

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    //ローカルデータベースへの操作内容

    //データを一つずつ挿入するための関数
    suspend fun insert(infoList: InfoList): Boolean{
        infoListDao.insert(infoList)
        return true
    }

    //データをアップデートするための関数
    suspend fun update(infoList: MutableList<InfoList>): Boolean{
        val list = infoListDao.update(infoList)
        Log.d("updating", list.toString())
        return true
    }

    //タイトルを変更するための関数
    fun changeTitle(oldTitle: String, newTitle: String): Boolean{
        infoListDao.changeTitle(oldTitle, newTitle)
        return true
    }

    //特定の要素を削除する関数
    suspend fun delete(infoList: InfoList): Boolean{
        infoListDao.delete(infoList)
        return true
    }

    //リストに記載されている要素を削除する関数
    suspend fun deleteList(infoList: MutableList<InfoList>): Boolean{
        infoListDao.deleteList(infoList)
        return true
    }

    //指定したタイトル名のものを削除する関数
    suspend fun deleteWithTitle(title:String): Boolean{
        infoListDao.deleteWithTitle(title)
        return true
    }
}