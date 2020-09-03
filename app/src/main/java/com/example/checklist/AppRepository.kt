package com.example.checklist

import androidx.lifecycle.LiveData

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    fun insert(list: InfoList){
        infoListDao.insert(list)
    }
}