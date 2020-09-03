package com.example.checklist

import androidx.lifecycle.LiveData

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    fun insert(infoList: InfoList){
        infoListDao.insert(infoList)
    }

    fun changeCheck(check: Boolean, item: String){
        infoListDao.changeCheck(check, item)
    }
}