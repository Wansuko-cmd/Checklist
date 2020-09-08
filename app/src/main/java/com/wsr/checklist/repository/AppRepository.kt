package com.wsr.checklist.repository

import androidx.lifecycle.LiveData
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.info_list_database.InfoListDao

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    fun insert(infoList: InfoList){
        infoListDao.insert(infoList)
    }

    fun changeCheck(id: String, check: Boolean){
        infoListDao.changeCheck(id, check)
    }

    fun deleteWithTitle(title:String){
        infoListDao.deleteWithTitle(title)
    }
}