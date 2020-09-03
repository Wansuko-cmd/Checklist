package com.example.checklist

import androidx.lifecycle.LiveData

class InfoListRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    suspend fun insert(list: InfoList){
        infoListDao.insert(list)
    }
}