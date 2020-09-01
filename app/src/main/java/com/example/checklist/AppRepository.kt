package com.example.checklist

import androidx.lifecycle.LiveData

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    suspend fun insert(check: Boolean, item: String){
       infoListDao.insert(check, item)
    }
}